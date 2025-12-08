package com.ssafy.foody.food.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AiFoodResponse {
    private String name;            // 음식 이름
    
    // AI가 추정한 영양소 정보 (100g 기준)
    private String standard;        // 기준 용량
    private Double kcal;
    private Double carb;
    private Double protein;
    private Double fat;
    private Double sugar;
    private Double natrium;
}
