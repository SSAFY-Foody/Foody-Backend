package com.ssafy.foody.food.service;

import java.util.List;

import com.ssafy.foody.common.dto.PageResponse;
import com.ssafy.foody.food.dto.FavoriteCodeResponse;
import com.ssafy.foody.food.dto.FavoriteResponse;
import com.ssafy.foody.food.dto.FoodResponse;

public interface FoodService {

	// 음식 리스트 조회 (페이지네이션)
	PageResponse<FoodResponse> getFoodList(int page, String keyword, String category);

	// 찜 추가
	void addFavorite(String userId, String foodCode, Integer userFoodCode);

	// 찜 삭제
	void deleteFavorite(int favoriteId);

	// 찜 조회
	PageResponse<FavoriteResponse> getFavoriteList(String userId, int page, String filter);
	
	// 모든 찜 코드 조회 (isFavorite 체크용)
	List<FavoriteCodeResponse> getAllFavoriteCodes(String userId);

	// 카테고리 목록 조회
	List<String> getCategories();

	// 사용자 입력 음식 목록 조회
	PageResponse<FoodResponse> getUserFoodList(String userId, int page);

}
