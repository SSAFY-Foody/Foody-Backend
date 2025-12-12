package com.ssafy.foody.admin.service;

import org.springframework.stereotype.Service;

import com.ssafy.foody.admin.mapper.AdminUserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	
	private final AdminUserMapper adminUserMapper;
	@Override
	public void updateUserRole(String userId) {
		int updated = adminUserMapper.updateUserRole(userId);
		
		if(updated == 0) {
			// 없는 유저이거나 업데이트 실패일 경우
			throw new IllegalArgumentException("해당 유저가 존재하지 않거나 권한을 변경할 수 없습니다");
		}

	}

}
