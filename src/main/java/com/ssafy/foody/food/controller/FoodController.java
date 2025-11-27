package com.ssafy.foody.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.food.dto.FoodInfo;
import com.ssafy.foody.food.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
	
	private final FoodService foodService;
	
	@GetMapping("/list")
	public ResponseEntity<List<FoodInfo>> getFoodList (
			@RequestParam(value = "page", defaultValue = "1") int page
			){
		
		List<FoodInfo> list = foodService.foodList(page);
		log.debug("조회된 데이터 {} " ,list);
		return ResponseEntity.ok(list);
	}
		
}
