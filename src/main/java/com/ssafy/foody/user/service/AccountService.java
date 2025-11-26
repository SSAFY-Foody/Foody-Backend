package com.ssafy.foody.user.service;

import com.ssafy.foody.user.dto.LoginResponse;
import com.ssafy.foody.user.dto.SignupRequest;

public interface AccountService {
	void signup(SignupRequest request); // 일반 회원가입
	LoginResponse authenticate(String id, String rawPassword); // 로그인 인증
}
