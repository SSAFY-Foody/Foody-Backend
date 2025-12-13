package com.ssafy.foody.admin.service;

import java.util.List;

import com.ssafy.foody.admin.dto.ActivityLevelResponse;
import com.ssafy.foody.admin.dto.UpdateActivityLevelRequest;
import com.ssafy.foody.food.dto.FoodRequest;

public interface AdminService {
	//권한 수정
	void updateUserRole(String userId, String role);
	
	//Foods테이블 새로운 값 삽입
	void addFood(FoodRequest food);
	
	//Foods 테이블에 있는 값 삭제하기
	void deleteFood(String code);
	
	//Activity Level 수정
	void updateActivityLevelByLevel(UpdateActivityLevelRequest activity);
	
	//Activity Level 전체 조회
	List<ActivityLevelResponse> findAllActivityLevels();
}
