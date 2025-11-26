package com.ssafy.foody.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private String accessToken; // JWT 토큰
	private String tokenType; // 토큰 타입 (보통 "Bearer" 고정)
	private String userId; // 로그인한 사용자 ID
	private String name;
	private String role;
}
