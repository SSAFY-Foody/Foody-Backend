package com.ssafy.foody.email.service;

public interface EmailService {

	// 인증 코드 발송
	void sendVerificationCode(String email);
	
	// 인증 코드 검증
	boolean verifyCode(String email, String code);
}
