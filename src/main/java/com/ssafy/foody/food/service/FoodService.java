package com.ssafy.foody.food.service;

import java.util.List;

import com.ssafy.foody.food.dto.FoodInfo;

public interface FoodService {
	List<FoodInfo> foodList(int page);
}
