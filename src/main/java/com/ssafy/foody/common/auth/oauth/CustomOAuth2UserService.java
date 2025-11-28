package com.ssafy.foody.common.auth.oauth;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserMapper userMapper;

	@Override
	@Transactional
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		// 소셜 서비스에서 유저 정보 가져오기
		OAuth2User oAuth2User = super.loadUser(userRequest);

		// 서비스 구분 (google, kakao)
		String registrationId = userRequest.getClientRegistration().getRegistrationId();

		// 유저 정보 추출
		Map<String, Object> attributes = oAuth2User.getAttributes();

		String providerId = "";
		String email = "";
		String name = "";

		// 로그인 처리
		if ("google".equals(registrationId)) {
			providerId = String.valueOf(attributes.get("sub")); // 구글의 고유 ID
			email = (String) attributes.get("email");
			name = (String) attributes.get("name");
		} else if ("kakao".equals(registrationId)) {
            // 카카오는 id가 숫자(Long)로 옴 -> String 변환
            providerId = String.valueOf(attributes.get("id"));
            
            // 이메일과 닉네임은 'kakao_account'라는 Map 안에 있음
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

            email = (String) kakaoAccount.get("email");
            name = (String) profile.get("nickname");
        }

		log.info("로그인 시도 - Provider: {}, ID: {}", registrationId, providerId);
		
		// DB 저장 또는 조회
		User user = saveOrUpdate(registrationId, providerId, email, name);

		// SecurityContext에 저장할 객체 반환
		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole())), attributes,
				"google".equals(registrationId) ? "sub" : "id" // userNameAttributeName (PK 역할)
		);
	}

	private User saveOrUpdate(String provider, String providerId, String email, String name) {
		// DB에서 provider_id로 유저 조회
		User user = userMapper.findByProviderId(providerId);

		// [신규 가입]
		if (user == null) {
			// ID 생성
			String uuid = UUID.randomUUID().toString().substring(0, 8);
			String generatedId = provider + "_" + uuid;

			user = User.builder()
					.id(generatedId)
					.name(name != null ? name : "소셜회원") // 구글 닉네임
					.email(email)
					.role("ROLE_GUEST") // GUEST는 추후에 추가 정보 입력
					.provider(provider)
					.providerId(providerId)
					.build();

			userMapper.save(user);
			log.info("소셜 회원가입 완료 (GUEST): {}", generatedId);
		} else {
			// [기존 회원] 로그 출력 정도만          
			log.info("소셜 로그인 성공: {}", user.getId());
		}

		return user;
	}
}