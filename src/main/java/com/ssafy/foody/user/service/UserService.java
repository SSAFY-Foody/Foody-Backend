package com.ssafy.foody.user.service;

import com.ssafy.foody.user.dto.UserResponse;
import com.ssafy.foody.user.dto.UserUpdateRequest;

public interface UserService {
	boolean isIdDuplicate(String id); // id 중복 체크
	UserResponse findById(String id); // 내 정보 조회 (id)
    void updateUserInfo(String userId, UserUpdateRequest request); // 회원 정보 수정
}
