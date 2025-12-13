package com.ssafy.foody.user.service;

import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.dto.UserUpdateRequest;

public interface UserService {

	// 내 정보 조회 (id)
	UserResponse findById(String userId);

	// 회원 정보 수정
	void updateUserInfo(String userId, UserUpdateRequest request);

	// 회원 탈퇴
	void deleteUser(String userId);

	// 비밀번호 변경
	void changePassword(String userId, String oldPassword, String newPassword); 

}
