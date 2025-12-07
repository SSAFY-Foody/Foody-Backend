package com.ssafy.foody.food.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.food.dto.FoodResponse;
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
	@Transactional(readOnly = true)
	public List<FoodResponse> getFoodList(int page, String keyword, String category) {
	    
	    int offset = (page - 1) * LIST_LIMIT;
	    
	    //검색어가 null이 안될때에만
	    if (keyword != null) {
	        keyword = keyword.trim();
	        // 빈 문자열이면 그냥 전체 조회하도록 처리
	        if (keyword.isEmpty()) {
	            keyword = null;
	        }
	    }

	    return foodMapper.selectFoodList(offset, LIST_LIMIT, keyword, category);
	}

}
