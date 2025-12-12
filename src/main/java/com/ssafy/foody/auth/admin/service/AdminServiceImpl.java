package com.ssafy.foody.auth.admin.service;

import org.springframework.stereotype.Service;

import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final UserMapper userMapper;
	
	public void updateUserRole(String userId, String role) {
		int updated = userMapper.updateRole(userId, role);
		
		if(updated == 0 ) {
			throw new IllegalArgumentException("해당 유저가 존재하지 않거나 권한을 변경할 수 없습니다");
		}
	}

}
