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
    private int id;             // PK
    private int mealId;         // FK (Meal)
    
    // 음식 출처 (둘 중 하나는 null)
    private String foodCode;    // DB 음식 코드
    private Integer userFoodCode; // 사용자 입력 음식 코드
    
    private double eatenWeight;    // 먹은 양
    
    // 음식 상세 정보 (JOIN으로 가져온 데이터)
    private String foodName;       // DB 음식 이름
    private String foodCategory;   // DB 음식 카테고리
    private String userFoodName;   // 사용자 입력 음식 이름
    
    // 계산된 영양소 값 (먹은 양 기준)
    private Double eatenKcal;
    private Double eatenCarb;
    private Double eatenProtein;
    private Double eatenFat;
    private Double eatenSugar;
    private Double eatenNatrium;
    
}