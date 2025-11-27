package com.ssafy.foody.food.service;

import java.util.List;

import com.ssafy.foody.food.dto.FoodInfo;

public interface FoodService {
	
//	음식 리스트 조회 (오프셋 페이지 넘기기) )
	List<FoodInfo> foodList(int page);
}
