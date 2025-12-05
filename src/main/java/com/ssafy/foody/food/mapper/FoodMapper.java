package com.ssafy.foody.food.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.food.domain.Food;
import com.ssafy.foody.food.domain.UserFood;
import com.ssafy.foody.food.dto.FoodResponse;

@Mapper
public interface FoodMapper {

	// 검색어(foodname)는 null일 수 있음
	List<FoodResponse> selectFoodList(@Param("offset") int offset, @Param("limit") int limit,
			@Param("keyWord") String keyWord, @Param("category") String category);

	// 음식 코드(PK)로 조회
	Food findFoodByCode(@Param("code") String code);
	
	// 사용자 입력 음식 저장
    void saveUserFood(UserFood userFood);
}
