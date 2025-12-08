package com.ssafy.foody.food.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.foody.food.dto.AiFoodResponse;
import com.ssafy.foody.food.dto.FoodResponse;

public interface FoodService {
	
	//	음식 리스트 조회 (페이지 넘기기) )
	List<FoodResponse> getFoodList(int page, String keyword, String category);
	
	// 이미지 분석 요청
    AiFoodResponse analyzeImage(MultipartFile image);
}
