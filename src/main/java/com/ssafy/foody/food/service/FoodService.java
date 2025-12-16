package com.ssafy.foody.food.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.foody.food.dto.AiFoodResponse;
import com.ssafy.foody.food.dto.FavoriteResponse;
import com.ssafy.foody.food.dto.FoodListResponse;
import com.ssafy.foody.food.dto.FoodResponse;

public interface FoodService {

	// 음식 리스트 조회 (페이지 넘기기)
	FoodListResponse getFoodList(int page, String keyword, String category);

	// 찜 추가
	void addFavorite(String userId, String foodCode, Integer userFoodCode);

	// 찜 삭제
	void deleteFavorite(int favoriteId);

	// 찜 조회
	List<FavoriteResponse> getFavoriteList(String userId);

	// 카테고리 목록 조회
	List<String> getCategories();

}
