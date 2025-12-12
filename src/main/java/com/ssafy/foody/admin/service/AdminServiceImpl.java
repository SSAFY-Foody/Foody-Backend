package com.ssafy.foody.admin.service;

import org.springframework.stereotype.Service;

import com.ssafy.foody.food.dto.FoodRequest;
import com.ssafy.foody.food.mapper.FoodMapper;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
	private final UserMapper userMapper;
	private final FoodMapper foodMapper;
	
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

	@Override
	
	public void addFood(FoodRequest food) {
		// 유호성 검사
		if (food == null) {
			throw new IllegalArgumentException("등록할 음식 정보가 없습니다.");
		}
		// 중복 검사 (이미 등록된 음식정보인지)
		int existCode = foodMapper.checkFoodExists(food.getCode());
		log.info("중복 값 확인 : {}", existCode);
		if (existCode > 0) {
			throw new IllegalArgumentException("이미 등록된 음식 코드입니다");
		}
		
		//음식 등록
		foodMapper.addFood(food);
	}

}
