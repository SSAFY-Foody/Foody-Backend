package com.ssafy.foody.food.service;

import java.util.List;

import com.ssafy.foody.food.dto.FoodResponse;

public interface FoodService {
	
	//	음식 리스트 조회 (페이지 넘기기) )
	List<FoodResponse> getFoodList(int page, String foodname, String category);
	
}
