package com.ssafy.foody.report.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ReportRequest {
	
	// 전문가 분석 대기 여부 (true: 전문가에게 요청, false: 바로 결과 보기(AI 분석))
	@NotNull(message = "분석 요청 정보를 설정해주세요.")
	private Boolean isWaited;
	
	@NotEmpty(message = "식단 정보는 최소 1개 이상이어야 합니다.")
    @Valid
    private List<MealRequest> meals;

    @Data
    public static class MealRequest {
    	// 대소문자 구분함, 정확히 일치해야 통과
        @Pattern(regexp = "BREAKFAST|LUNCH|DINNER|SNACK", 
                 message = "식사 유형은 BREAKFAST, LUNCH, DINNER, SNACK 중 하나여야 합니다.")
        private String mealType;
    
        @NotEmpty(message = "음식 목록은 비어있을 수 없습니다.")
        private List<FoodItem> foods;
    }

    @Data
    public static class FoodItem {
        private String foodCode; // DB 음식일 때만 값 있음 (없으면 null)
        
        @NotBlank(message = "음식 이름은 필수입니다.")
        private String name;     // 음식 이름
        
        @Positive(message = "먹은 중량은 0보다 커야 합니다.")
        private double eatenWeight; // 먹은 중량
        
        // --- 영양소 정보 (사용자 입력 시에만 사용) ---
        @Positive(message = "기준 중량은 0이상이어야 합니다.")
        private Double standard; // 영양소 기준 중량 (g 또는 ml)
        // 음수는 안 됨
        @PositiveOrZero(message = "칼로리는 음수일 수 없습니다.")
        private Double kcal;

        @PositiveOrZero(message = "탄수화물은 음수일 수 없습니다.")
        private Double carb;

        @PositiveOrZero(message = "단백질은 음수일 수 없습니다.")
        private Double protein;

        @PositiveOrZero(message = "지방은 음수일 수 없습니다.")
        private Double fat;

        @PositiveOrZero(message = "당은 음수일 수 없습니다.")
        private Double sugar;

        @PositiveOrZero(message = "나트륨은 음수일 수 없습니다.")
        private Double natrium;
    }
}
