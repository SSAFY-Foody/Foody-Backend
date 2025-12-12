package com.ssafy.foody.food.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.food.domain.Favorite;
import com.ssafy.foody.food.domain.Food;
import com.ssafy.foody.food.domain.UserFood;
import com.ssafy.foody.food.dto.FavoriteResponse;
import com.ssafy.foody.food.dto.FoodRequest;
import com.ssafy.foody.food.dto.FoodResponse;

@Mapper
public interface FoodMapper {

	// 검색어(foodname)는 null일 수 있음
	List<FoodResponse> selectFoodList(@Param("offset") int offset, @Param("limit") int limit,
			@Param("keyword") String keyword, @Param("category") String category);

	// 음식 코드(PK)로 조회
	Food findFoodByCode(@Param("code") String code);
	
	// 사용자 입력 음식 저장
    void saveUserFood(UserFood userFood);
    
    // 찜하기
    void insertFavorite(Favorite favorite);

    // 찜 취소
    void deleteFavorite(int favoriteId);

    // 찜 목록 조회
    List<FavoriteResponse> selectFavoriteList(String userId);
    
    // 중복 찜 방지용 확인
    Integer checkFavoriteExists(@Param("userId") String userId, 
                                @Param("foodCode") String foodCode, 
                                @Param("userFoodCode") Integer userFoodCode);
    
    //Food Table 에 음식 정보 입력 (관리자 권한 (ADMIN))
    void addFood(FoodRequest food);
    
    //음식 중복 등록 방지용
    int checkFoodExists(@Param("code") String code);
}
