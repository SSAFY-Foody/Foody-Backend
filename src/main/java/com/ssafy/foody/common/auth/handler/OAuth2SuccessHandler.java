package com.ssafy.foody.global.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.ssafy.foody.global.auth.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        
        // 인증된 유저 정보 가져오기
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        
        // JWT 토큰 생성 (Access Token)
        String token = jwtTokenProvider.createToken(oAuth2User.getAttribute("email"));

        // 프론트엔드로 리다이렉트 (토큰을 쿼리 파라미터로 전달)
        String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000/oauth/callback")
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
