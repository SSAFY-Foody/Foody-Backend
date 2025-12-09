package com.ssafy.foody.report.dto;

import java.util.List;

import com.ssafy.foody.user.domain.StdInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiReportRequest {

    // 유저 정보
    private StdInfo stdInfo;                  // 유저 표준 정보
    private String userActivityLevelDesc;     // 유저 활동량 설명

    // 하루치 종합 정보
    private double dayTotalKcal;
    private double dayTotalCarb;
    private double dayTotalProtein;
    private double dayTotalFat;
    private double dayTotalSugar;
    private double dayTotalNatrium;

    // 끼니 정보
    private List<MealInfo> meals;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealInfo {
        private String mealType;        // BREAKFAST, LUNCH, DINNER, SNACK
        
        // 끼니 별 종합 정보
        private double totalKcal;
        private double totalCarb;
        private double totalProtein;
        private double totalFat;
        private double totalSugar;
        private double totalNatrium;

        // 해당 끼니에 먹은 음식 정보
        private List<FoodInfo> foods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodInfo {
        private String name;            // 음식 이름
        
        private double eatenWeight;     // 먹은 양
        
        // 음식 개별 영양소
        private double eatenKcal;
        private double eatenCarb;
        private double eatenProtein;
        private double eatenFat;
        private double eatenSugar;
        private double eatenNatrium;
    }
}