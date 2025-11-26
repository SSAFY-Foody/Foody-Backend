package com.ssafy.foody.user.service;

import com.ssafy.foody.user.dto.UserResponse;

public interface UserService {
	boolean isIdDuplicate(String id); // id 중복 체크
	UserResponse findById(String id); // 내 정보 조회 (id)
}
