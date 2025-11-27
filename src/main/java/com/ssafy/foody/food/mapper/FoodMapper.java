package com.ssafy.foody.food.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.food.domain.Food;

@Mapper
public interface FoodMapper {
	
	//음식 리스트 오프셋으로 조회
	List<Food> foodList(@Param("offset") int offset, @Param("limit") int limit);
	
}
