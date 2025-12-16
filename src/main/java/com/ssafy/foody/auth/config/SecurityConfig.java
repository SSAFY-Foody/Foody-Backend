package com.ssafy.foody.auth.config;

import org.springframework.beans.factory.annotation.Value;
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

import com.ssafy.foody.auth.filter.AdminAccessFilter;
import com.ssafy.foody.auth.filter.ReportAccessFilter;
import com.ssafy.foody.auth.handler.OAuth2SuccessHandler;
import com.ssafy.foody.auth.jwt.JwtAuthenticationFilter;
import com.ssafy.foody.auth.jwt.JwtTokenProvider;
import com.ssafy.foody.auth.oauth.CustomOAuth2UserService;
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

	// front 서버 BASE URL
	@Value("${front.server.base.url}")
	private String frontServerBaseUrl;

	// front 서버 port
	@Value("${front.server.port}")
	private String frontServerPort;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// CSRF 비활성화 (REST API이므로)
				.csrf(AbstractHttpConfigurer::disable)

				// CORS 설정 활성화
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// 세션 안 씀 (JWT로 대체)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

				// 요청 권한 설정 (requestMatchers 메서드 사용)
				.authorizeHttpRequests(auth -> auth
						// Swagger 관련 주소
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
						// '/food/auth/**' 패턴은 로그인 필요 (찜하기, 삭제, 조회)
						.requestMatchers("/food/auth/**").authenticated()
						// admin 전용 유저 API
						.requestMatchers("/admin/**").hasRole("ADMIN")
						// 권한 없이 주소 허용
						.requestMatchers("/error/**", "/account/**", "/oauth2/**", "/email/**", "/login/**", "/food/**",
								"/favicon.ico", "/character/**")
						.permitAll()
						// 권한 필요
						.anyRequest().authenticated())

				// OAuth2 로그인 설정
				.oauth2Login(oauth -> oauth.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
						.successHandler(oAuth2SuccessHandler))

				// JWT 필터
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userMapper),
						UsernamePasswordAuthenticationFilter.class)
				// GUEST 레포트 접근 제한 필터
				.addFilterAfter(new ReportAccessFilter(), JwtAuthenticationFilter.class)
				// ADMIN 접근 제한 필터
				.addFilterAfter(new AdminAccessFilter(), JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
		org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();

		// 허용할 Origin (프론트엔드 주소)
		String frontendURL = String.format("%s:%s", frontServerBaseUrl, frontServerPort);
		configuration.addAllowedOrigin(frontendURL);

		// 허용할 HTTP 메서드
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("PATCH");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("OPTIONS");

		// 허용할 헤더
		configuration.addAllowedHeader("*");

		// 인증 정보 포함 허용
		configuration.setAllowCredentials(true);

		// 모든 경로에 CORS 설정 적용
		org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
