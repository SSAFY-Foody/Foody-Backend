package com.ssafy.foody.report.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private int id;
    private String userId;

    // 분석 정보
    private Double score;
    private String comment;
    private Integer characterId;
    private boolean isWaited;

    // 하루 섭취 영양소 총합
    private Double totalKcal;
    private Double totalCarb;
    private Double totalProtein;
    private Double totalFat;
    private Double totalSugar;
    private Double totalNatrium;

    // 생성 당시 유저 신체 정보
    private Integer userAge;
    private Double userHeight;
    private Double userWeight;
    private String userGender;
    private Integer userActivityLevel;

    // 생성 당시 유저 표준 정보
    private Double userStdWeight;
    private Double userStdKcal;
    private Double userStdCarb;
    private Double userStdProtein;
    private Double userStdFat;
    private Double userStdSugar;
    private Double userStdNatrium;

    private LocalDateTime createdAt;

    // 끼니별 정보
    private List<MealResponse> meals;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealResponse {
        private int id;
        private String mealType;
        private Double totalKcal;
        private Double totalCarb;
        private Double totalProtein;
        private Double totalFat;
        private Double totalSugar;
        private Double totalNatrium;
        private List<MealFoodResponse> mealFoods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MealFoodResponse {
        private int id;
        private double eatenWeight;

        // 음식 정보
        private String foodName;
        private String foodCategory;
        private String userFoodName;

        // 계산된 영양소 (먹은 양 기준)
        private Double eatenKcal;
        private Double eatenCarb;
        private Double eatenProtein;
        private Double eatenFat;
        private Double eatenSugar;
        private Double eatenNatrium;
    }
    
    // 기본형 Getter/Setter 직접 선언 (네이밍 불일치 해결) - 혹은 Boolean 써도 됨
    public boolean getIsWaited() {
        return isWaited;
    }

    public void setIsWaited(boolean isWaited) {
        this.isWaited = isWaited;
    }
}
