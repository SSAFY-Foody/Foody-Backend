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

import com.ssafy.foody.common.dto.PageResponse;
import com.ssafy.foody.food.dto.AiFoodResponse;
import com.ssafy.foody.food.dto.FavoriteCodeResponse;
import com.ssafy.foody.food.dto.FavoriteRequest;
import com.ssafy.foody.food.dto.FavoriteResponse;
import com.ssafy.foody.food.dto.FoodResponse;
import com.ssafy.foody.food.service.AiFoodService;
import com.ssafy.foody.food.service.FoodService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@Tag(name = "Food", description = "음식 및 찜하기 API")
public class FoodController {

    private final FoodService foodService;
    private final AiFoodService aiFoodService;

    @Operation(summary = "음식 검색 및 목록 조회", description = "음식을 검색하거나 전체 목록을 조회합니다. (페이지네이션)")
    @GetMapping("/")
    public ResponseEntity<PageResponse<FoodResponse>> getFoodList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "keyword", required = false) String keyword, // 검색어가 없어도 동작하도록 설정
            @RequestParam(value = "category", required = false) String category // 카테고리
    ) {
        log.debug("조회된 음식 : {}", keyword);
        log.debug("조호된 페이지 : {}", page);
        // Service에 페이지 번호와 검색어(있다면)를 넘김
        PageResponse<FoodResponse> response = foodService.getFoodList(page, keyword, category);

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
    @Operation(summary = "음식 카테고리 조회", description = "전체 음식 카테고리 목록을 조회합니다.")
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
    @Operation(summary = "음식 이미지 분석", description = "음식 사진을 업로드하여 AI 분석 결과를 받습니다.")
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
    @Operation(summary = "찜 목록 추가", description = "음식을 찜 목록에 추가합니다.")
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
    @Operation(summary = "찜 목록 삭제", description = "찜 목록에서 음식을 제거합니다.")
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
    @Operation(summary = "찜 목록 조회", description = "나의 찜 목록을 조회합니다.")
    @GetMapping("/auth/favorite")
    public ResponseEntity<PageResponse<FavoriteResponse>> getFavoriteList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String filter) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(foodService.getFavoriteList(userDetails.getUsername(), page, filter));
    }

    /**
     * 모든 찜 코드 조회 (Check용)
     * GET /food/auth/favorite/codes
     */
    @Operation(summary = "전체 찜 코드 조회", description = "내가 찜한 모든 음식의 코드를 조회합니다.")
    @GetMapping("/auth/favorite/codes")
    public ResponseEntity<List<FavoriteCodeResponse>> getAllFavoriteCodes(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(foodService.getAllFavoriteCodes(userDetails.getUsername()));
    }

    /**
     * 사용자 입력 음식 목록 조회
     * GET /food/auth/user-food
     */
    @Operation(summary = "사용자 직접 입력 음식 조회", description = "내가 직접 등록한 음식 목록을 조회합니다.")
    @GetMapping("/auth/user-food")
    public ResponseEntity<PageResponse<FoodResponse>> getUserFoodList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "1") int page) {
        if (userDetails == null)
            return ResponseEntity.status(401).build();

        return ResponseEntity.ok(foodService.getUserFoodList(userDetails.getUsername(), page));
    }

}
