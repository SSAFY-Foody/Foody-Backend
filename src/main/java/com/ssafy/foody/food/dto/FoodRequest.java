package com.ssafy.foody.food.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRequest {

    @NotBlank(message = "음식 코드는 필수입니다.")
    @Size(max = 50, message = "음식 코드는 45자 이하여야 합니다.")
    private String code;

    @NotBlank(message = "음식 이름은 필수입니다.")
    @Size(max = 30, message = "음식 이름은 30자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "카테고리는 필수입니다.")
    @Size(max = 30, message = "카테고리는 30자 이하여야 합니다.")
    private String category;

    @NotBlank(message = "기준량은 필수입니다.")
    @Size(max = 10, message = "기준량은 50자 이하여야 합니다.")
    private String standard;

    @PositiveOrZero(message = "kcal은 0 이상이어야 합니다.")
    private Double kcal;

    @PositiveOrZero(message = "carb은 0 이상이어야 합니다.")
    private Double carb;

    @PositiveOrZero(message = "protein은 0 이상이어야 합니다.")
    private Double protein;

    @PositiveOrZero(message = "fat은 0 이상이어야 합니다.")
    private Double fat;

    @PositiveOrZero(message = "sugar은 0 이상이어야 합니다.")
    private Double sugar;

    @PositiveOrZero(message = "natrium은 0 이상이어야 합니다.")
    private Double natrium;
}
