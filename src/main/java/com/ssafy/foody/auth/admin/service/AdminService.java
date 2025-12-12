package com.ssafy.foody.auth.admin.service;

public interface AdminService {
	//권한 수정
	void updateUserRole(String userId, String role);
}
