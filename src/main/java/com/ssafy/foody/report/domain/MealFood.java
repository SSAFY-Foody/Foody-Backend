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
    
}