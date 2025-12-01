package com.ssafy.foody.food.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.oauth2.sdk.Response;
import com.ssafy.foody.food.dto.FoodResponse;
import com.ssafy.foody.food.service.FoodService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
	
	private final FoodService foodService;
	
	@GetMapping("/select")
	public ResponseEntity<List<FoodResponse>> getFoodList(
	        @RequestParam(value = "page", defaultValue = "1") int page,
	        @RequestParam(value = "foodname", required = false) String foodname // 검색어가 없어도 동작하도록 설정
	) {
	    log.debug("조회된 음식 : {}", foodname);
	    log.debug("조호된 페이지 : {}", page);
	    // Service에 페이지 번호와 검색어(있다면)를 넘김
	    List<FoodResponse> list = foodService.getFoodList(page, foodname);

	    // 데이터가 없으면 204
	    if (list == null || list.isEmpty()) {
	        // 검색어가 있었는데 결과가 없는 경우와 그냥 데이터가 없는 경우 모두 포함
	        return ResponseEntity.noContent().build();
	    }

	    return ResponseEntity.ok(list);
	}
		
}
