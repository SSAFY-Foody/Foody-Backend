package com.ssafy.foody.food.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.foody.food.dto.AiFoodResponse;
import com.ssafy.foody.food.dto.FavoriteRequest;
import com.ssafy.foody.food.dto.FavoriteResponse;
import com.ssafy.foody.food.dto.FoodListResponse;
import com.ssafy.foody.food.service.AiFoodService;
import com.ssafy.foody.food.service.FoodService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final AiFoodService aiFoodService;

    @GetMapping("/")
    public ResponseEntity<FoodListResponse> getFoodList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword, // 검색어가 없어도 동작하도록 설정
            @RequestParam(value = "category", required = false) String category // 카테고리
    ) {
        log.debug("조회된 음식 : {}", keyword);
        log.debug("조호된 페이지 : {}", page);
        // Service에 페이지 번호와 검색어(있다면)를 넘김
        FoodListResponse response = foodService.getFoodList(page, keyword, category);

        // 데이터가 없으면 204
        if (response.getContent() == null || response.getContent().isEmpty()) {
            // 검색어가 있었는데 결과가 없는 경우와 그냥 데이터가 없는 경우 모두 포함
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 음식 카테고리 목록 조회 (Public)
     * 인증 없이 조회 가능
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = foodService.getCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * 음식 이미지 분석 요청
     * POST /food/analyze
     * form-data key: "image" (파일)
     */
    @PostMapping(value = "/analyze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AiFoodResponse> analyzeFoodImage(
            @RequestParam("image") MultipartFile image) {
        log.info("이미지 분석 요청 받음: {}", image.getOriginalFilename());

        AiFoodResponse result = aiFoodService.analyzeImage(image);

        return ResponseEntity.ok(result);
    }

    /**
     * 찜 목록 추가
     * POST /food/auth/favorite
     */
    @PostMapping("/auth/favorite")
    public ResponseEntity<String> addFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody FavoriteRequest request) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        foodService.addFavorite(
                userDetails.getUsername(),
                request.getFoodCode(),
                request.getUserFoodCode());

        return ResponseEntity.ok("찜 목록에 추가되었습니다.");
    }

    /**
     * 찜 목록 삭제
     * DELETE /food/auth/favorite?favoriteId=...
     */
    @DeleteMapping("/auth/favorite")
    public ResponseEntity<String> deleteFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam int favoriteId) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        foodService.deleteFavorite(favoriteId);
        return ResponseEntity.ok("찜 목록에서 삭제되었습니다.");
    }

    /**
     * 찜 목록 조회
     * GET /food/auth/favorite
     */
    @GetMapping("/auth/favorite")
    public ResponseEntity<List<FavoriteResponse>> getFavoriteList(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(foodService.getFavoriteList(userDetails.getUsername()));
    }
}
