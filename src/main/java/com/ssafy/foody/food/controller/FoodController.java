package com.ssafy.foody.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.Response;
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
	
	// 음식 목록 조회 한 페이지당 50개로 설정
	@GetMapping("/list")
	public ResponseEntity<List<FoodInfo>> getFoodList (
			@RequestParam(value = "page", defaultValue = "1") int page
			){
		
		List<FoodInfo> list = foodService.foodList(page);
//		log.debug("조회된 데이터 {} " ,list);
//		log.debug("조호된 데이터 개수 {}",  list.size());
		
		//예외 처리 데이터 페이지 개수보다 더 많은 페이지를 요청한 경우
		if(list ==null || list.isEmpty()) {
			log.debug("요청한 페이지 ({})에 데이터가 없다", page);
			return ResponseEntity.noContent().build(); //데이터 없으면 204 코드 반환
		}
		
		
		return ResponseEntity.ok(list);
	}
	
	// 음식 검색 기능
	@PostMapping("/search")
	public ResponseEntity<List<FoodInfo>> searchFood (
			@RequestParam(value = "foodname") String foodname
			) {
		
		// 예외 처리 : 빈 문자열일 경우
		if(foodname == null || foodname.trim().isEmpty()) {
//			log.debug("빈 문자열 입니다 : {}" ,foodname);
			return ResponseEntity.badRequest().build(); // 400 Bad Request
		}
		
		
		List<FoodInfo> list = foodService.foodSearch(foodname);
//		log.debug("검색된 음식 {} ", list);
		
		//예외 처리 204
		if(list == null || list.isEmpty()) {
//			log.debug("검색된 음식이 없습니다: {}", foodname);
			return ResponseEntity.noContent().build(); // 204 No Content(데이터 없음 에러 처리)
		}
		
		log.debug("검색된 음식 {} 개", list.size());
		return ResponseEntity.ok(list);
	}
		
}
