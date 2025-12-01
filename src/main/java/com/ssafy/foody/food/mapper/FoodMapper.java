package com.ssafy.foody.food.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.food.domain.Food;
import com.ssafy.foody.food.dto.FoodResponse;

@Mapper
public interface FoodMapper {
	
	// 검색어(foodname)는 null일 수 있음
    List<FoodResponse> selectFoodList(
        @Param("offset") int offset, 
        @Param("limit") int limit, 
        @Param("foodname") String foodname
    );
}
