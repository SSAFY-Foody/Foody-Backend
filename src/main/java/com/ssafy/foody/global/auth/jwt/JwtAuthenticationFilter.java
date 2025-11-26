package com.ssafy.foody.global.auth.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.foody.user.mapper.UserMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserMapper userMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// 헤더에서 토큰 꺼내기
		String token = jwtTokenProvider.resolveToken(request);

		// 토큰이 있고 유효하다면
		if (token != null && jwtTokenProvider.validateToken(token)) {
			String userId = jwtTokenProvider.getUserPk(token);

			// DB 유저 정보 조회
			com.ssafy.foody.user.domain.User dbUser = userMapper.findById(userId);

			if (dbUser != null) {
				UserDetails principal = User.builder()
						.username(dbUser.getId())
						.password(dbUser.getPassword())
						.authorities(dbUser.getRole())
						.build();

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal,
						null, principal.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				log.warn("토큰은 유효하지만 DB에 해당 유저가 없습니다. ID: {}", userId);
			}
		}

		// 다음 필터로 넘기기
		chain.doFilter(request, response);
	}
}
