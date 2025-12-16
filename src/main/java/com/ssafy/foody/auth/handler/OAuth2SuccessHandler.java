package com.ssafy.foody.auth.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssafy.foody.auth.jwt.JwtTokenProvider;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    
	// front 서버 BASE URL
	@Value("${front.server.base.url}")
    private String frontServerBaseUrl;
	
	// front 서버 port
	@Value("${front.server.port}")
    private String frontServerPort;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        // 인증된 유저 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 소셜 정보 확인
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId(); // "google" or "kakao"

        // 소셜 정보에 따라 ID 꺼내기
        String providerId = null;
        if ("google".equals(registrationId)) {
            providerId = String.valueOf(attributes.get("sub"));
        } else if ("kakao".equals(registrationId)) {
            providerId = String.valueOf(attributes.get("id"));
        }

        // 유저 조회
        User dbUser = userMapper.findByProviderId(providerId);

        if (dbUser == null) {
            log.error("소셜 로그인 에러: DB에서 유저를 찾을 수 없습니다. ProviderId: {}", providerId);
            throw new IllegalStateException("USER_NOT_FOUND");
        }

        // JWT 토큰 생성 (Access Token)
        String token = jwtTokenProvider.createToken(dbUser.getId());

        // 프론트엔드로 리다이렉트 (토큰을 쿼리 파라미터로 전달)
        String redirectURL = String.format("%s:%s/oauth/callback", frontServerBaseUrl, frontServerPort);
        String targetUrl = UriComponentsBuilder.fromUriString(redirectURL)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
