package com.ssafy.foody.auth.admin.service;

import org.springframework.stereotype.Service;

import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
	private final UserMapper userMapper;
	
	public void updateUserRole(String userId, String role) {
		
		String normalizedRole = normalize(role);
		
		int updated = userMapper.updateRole(userId, normalizedRole);
		
		if(updated == 0 ) {
			throw new IllegalArgumentException("해당 유저가 존재하지 않거나 권한을 변경할 수 없습니다");
		}
	}
	
	// 권한 명 제대로 되었는지 검사하는 메서드
	private String normalize(String role) {
		if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("권한 값이 비어있습니다.");
        }

        // 대소문자 통일
        role = role.trim().toUpperCase();

        // ROLE_ 접두어 없으면 붙이기
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        // 허용된 권한만 통과
        if (!"ROLE_USER".equals(role) && !"ROLE_ADMIN".equals(role)) {
            throw new IllegalArgumentException("허용되지 않은 권한입니다: " + role);
        }

        return role;
	}

}
