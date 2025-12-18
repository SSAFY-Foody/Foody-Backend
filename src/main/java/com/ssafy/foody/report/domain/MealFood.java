package com.ssafy.foody.report.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealFood {
    private int id; // PK
    private int mealId; // FK (Meal)

    // 음식 출처 (둘 중 하나는 null)
    private String foodCode; // DB 음식 코드
    private Integer userFoodCode; // 사용자 입력 음식 코드

    private double eatenWeight; // 먹은 양

    // 음식 상세 정보
    private String foodName; // DB 음식 이름
    private String foodCategory; // DB 음식 카테고리
    private String userFoodName; // 사용자 입력 음식 이름

    // 원본 음식 정보 - DB foods 테이블
    private String foodStandard; // 기준량 (예: "100g", "200ml")
    private Double foodKcal;
    private Double foodCarb;
    private Double foodProtein;
    private Double foodFat;
    private Double foodSugar;
    private Double foodNatrium;

    // 원본 음식 정보 - user_foods 테이블
    private String userFoodStandard;
    private Double userFoodKcal;
    private Double userFoodCarb;
    private Double userFoodProtein;
    private Double userFoodFat;
    private Double userFoodSugar;
    private Double userFoodNatrium;

}