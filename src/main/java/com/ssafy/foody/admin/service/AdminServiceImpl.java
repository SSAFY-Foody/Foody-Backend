package com.ssafy.foody.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.admin.dto.ActivityLevelResponse;
import com.ssafy.foody.admin.dto.UpdateActivityLevelRequest;
import com.ssafy.foody.food.domain.Food;
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
	
	@Override
	@Transactional
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
	@Transactional
	public void addFood(FoodRequest food) {
		// 유효성 검사
		if (food == null) {
			throw new IllegalArgumentException("등록할 음식 정보가 없습니다.");
		}
		// 중복 검사 (이미 등록된 음식정보인지)
		Food foodResponse = foodMapper.findFoodByCode(food.getCode());
		log.debug("중복 값 확인 : {}", foodResponse);
		if (foodResponse != null) {
			throw new IllegalArgumentException("이미 등록된 음식 코드입니다");
		}
		
		//음식 등록
		foodMapper.addFood(food);
	}

	@Override
	@Transactional
	public void deleteFood(String code) {
		//유효성 검사
		if(code == null) {
			throw new IllegalArgumentException("입력한 음식값이 존재하지 않습니다");
		}
		Food foodResponse = foodMapper.findFoodByCode(code);
		
		log.debug("삭제하려는 음식 정보 : {}", foodResponse);
		// 유효성 검사
		if(foodResponse == null) {
			throw new IllegalArgumentException("삭제하려는 음식 정보가 존재하지 않습니다");
		}
		
		//음식 삭제
		foodMapper.deleteFoodByCode(code);
	}

	@Override
	@Transactional
	public void updateActivityLevelByLevel(UpdateActivityLevelRequest request) {
		//유효성 검사
		if(request == null) {
			throw new IllegalArgumentException("수정할 활동 내용이 없습니다.");
		}
		
		//활동 권한 수정
		int updated = userMapper.updateActivityLevelByLevel(request);
		
		if(updated == 0) {
			throw new IllegalArgumentException("해당 활동 정보가 존재지 않거나 변경할  수 없습니다.");
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ActivityLevelResponse> findAllActivityLevels() {
		return userMapper.findAllActivityLevels();
	}

}
