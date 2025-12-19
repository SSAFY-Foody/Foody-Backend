package com.ssafy.foody.food.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.common.dto.PageResponse;
import com.ssafy.foody.food.domain.Favorite;
import com.ssafy.foody.food.dto.FavoriteCodeResponse;
import com.ssafy.foody.food.dto.FavoriteResponse;
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
    private static final int LIST_LIMIT = 8;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodResponse> getFoodList(int page, String keyword, String category) {

        int offset = (page - 1) * LIST_LIMIT;

        // 검색어가 null이 안될때에만
        if (keyword != null) {
            keyword = keyword.trim();
            // 빈 문자열이면 그냥 전체 조회하도록 처리
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }

        // 음식 리스트 조회
        List<FoodResponse> list = foodMapper.selectFoodList(offset, LIST_LIMIT, keyword, category);

        // 전체 개수 조회
        int totalCount = foodMapper.countFoodList(keyword, category);

        // 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / LIST_LIMIT);

        return PageResponse.<FoodResponse>builder()
                .content(list)
                .page(page)
                .size(LIST_LIMIT)
                .totalElements(totalCount)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional
    public void addFavorite(String userId, String foodCode, Integer userFoodCode) {
        // 유효성 검사
        if (foodCode == null && userFoodCode == null) {
            throw new IllegalArgumentException("찜할 음식 정보가 없습니다.");
        }

        // 유효성 검사
        if (foodCode != null && userFoodCode != null) {
        	log.debug("foodCode: {}, userFoodCode: {}", foodCode, userFoodCode);
            throw new IllegalArgumentException("찜할 음식 정보가 잘못 됐습니다.");
        }

        // 중복 검사 (이미 찜했는지)
        Integer existId = foodMapper.checkFavoriteExists(userId, foodCode, userFoodCode);
        if (existId != null) {
            throw new IllegalArgumentException("이미 찜한 음식입니다."); // 또는 그냥 리턴해서 무시
        }

        // 찜
        Favorite favorite = Favorite.builder()
                .userId(userId)
                .foodCode(foodCode) // DB 음식이면 값 있음, 아니면 null
                .userFoodCode(userFoodCode) // 사용자 음식이면 값 있음, 아니면 null
                .build();

        foodMapper.insertFavorite(favorite);
    }

    @Override
    @Transactional
    public void deleteFavorite(int favoriteId) {
        foodMapper.deleteFavorite(favoriteId);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FavoriteResponse> getFavoriteList(String userId, int page, String filter) {
        int offset = (page - 1) * LIST_LIMIT;
        
        List<FavoriteResponse> list = foodMapper.selectFavoriteList(userId, LIST_LIMIT, offset, filter);
        int totalCount = foodMapper.countFavoriteList(userId, filter);
        int totalPages = (int) Math.ceil((double) totalCount / LIST_LIMIT);
        
        return PageResponse.<FavoriteResponse>builder()
                .content(list)
                .page(page)
                .size(LIST_LIMIT)
                .totalElements(totalCount)
                .totalPages(totalPages)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteCodeResponse> getAllFavoriteCodes(String userId) {
        return foodMapper.selectAllFavoriteCodes(userId);
    }

    @Override
    public List<String> getCategories() {
        return foodMapper.selectDistinctCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<FoodResponse> getUserFoodList(String userId, int page) {
        int offset = (page - 1) * LIST_LIMIT;
        
        List<FoodResponse> list = foodMapper.selectUserFoodList(userId, LIST_LIMIT, offset);
        int totalCount = foodMapper.countUserFoodList(userId);
        int totalPages = (int) Math.ceil((double) totalCount / LIST_LIMIT);
        
        return PageResponse.<FoodResponse>builder()
                .content(list)
                .page(page)
                .size(LIST_LIMIT)
                .totalElements(totalCount)
                .totalPages(totalPages)
                .build();
    }

}
