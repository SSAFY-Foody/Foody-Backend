package com.ssafy.foody.food.service;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.foody.food.dto.AiFoodResponse;

public interface FoodAiService {
	
	// 이미지 분석 요청
    AiFoodResponse analyzeImage(MultipartFile image);
    
}
