package com.ssafy.foody.admin.service;

import com.ssafy.foody.food.dto.FoodRequest;

public interface AdminService {
	//권한 수정
	void updateUserRole(String userId, String role);
	
	//Foods테이블 새로운 값 삽입
	void addFood(FoodRequest food);
}
