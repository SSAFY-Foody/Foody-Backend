package com.ssafy.foody.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ssafy.foody.common.auth.handler.OAuth2SuccessHandler;
import com.ssafy.foody.common.auth.jwt.JwtAuthenticationFilter;
import com.ssafy.foody.common.auth.jwt.JwtTokenProvider;
import com.ssafy.foody.common.auth.oauth.CustomOAuth2UserService;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserMapper userMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// CSRF 비활성화 (REST API이므로)
				.csrf(AbstractHttpConfigurer::disable)

				// 세션 안 씀 (JWT로 대체)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 요청 권한 설정 (requestMatchers 메서드 사용)
				.authorizeHttpRequests(auth -> auth
//            	// food oauth 비활성화 코드
//                .requestMatchers("/food/**").permitAll()
						// Swagger 관련 주소
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
						// 주소 허용
						.requestMatchers("/error/**", "/account/**", "/oauth2/**", "/login/**", "/favicon.ico").permitAll()
						// 권한 필요
						.anyRequest().authenticated())

				// OAuth2 로그인 설정
				.oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
						.successHandler(oAuth2SuccessHandler))

				// JWT 필터
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userMapper),
						UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
