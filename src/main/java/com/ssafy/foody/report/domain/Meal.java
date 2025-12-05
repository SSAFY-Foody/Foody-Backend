package com.ssafy.foody.report.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    private int id;             // PK
    private int reportId;       // FK
    private String mealType;    // BREAKFAST, LUNCH, DINNER, SNACK
    
    // 끼니별 영양소 총합
    private Double totalKcal;
    private Double totalCarb;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugar;
    private Double totalNatrium;

    // 1:N 관계, 끼니별 음식 정보
    private List<MealFood> mealFoods;
}