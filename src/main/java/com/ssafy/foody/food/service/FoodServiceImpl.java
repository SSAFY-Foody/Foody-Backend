package com.ssafy.foody.food.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.foody.food.domain.Food;
import com.ssafy.foody.food.dto.FoodInfo;
import com.ssafy.foody.food.mapper.FoodMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

	private final FoodMapper foodMapper;
	
	// 한 페이지당 보여줄 개수 정의
	private static final int LIST_LIMIT = 50;
	
	@Override
	public List<FoodInfo> foodList(int page) {
		if(page < 1) page = 1;
		int offset = (page-1) * LIST_LIMIT;
		
		List<Food> voList = foodMapper.foodList(offset, LIST_LIMIT);
		
		return voList.stream().map(FoodInfo::new).collect(Collectors.toList());
	}

}
