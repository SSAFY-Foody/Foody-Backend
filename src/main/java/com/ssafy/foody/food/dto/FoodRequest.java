package com.ssafy.foody.food.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodRequest {

    @NotBlank(message = "code는 필수입니다.")
    @Size(max = 50, message = "code는 45자 이하여야 합니다.")
    private String code;

    @NotBlank(message = "name은 필수입니다.")
    @Size(max = 30, message = "name은 30자 이하여야 합니다.")
    private String name;

    @NotBlank(message = "category는 필수입니다.")
    @Size(max = 30, message = "category는 30자 이하여야 합니다.")
    private String category;

    @NotBlank(message = "standard는 필수입니다.")
    @Size(max = 10, message = "standard는 50자 이하여야 합니다.")
    private String standard;

    @PositiveOrZero(message = "kcal은 0 이상이어야 합니다.")
    private double kcal;

    @PositiveOrZero(message = "carb은 0 이상이어야 합니다.")
    private double carb;

    @PositiveOrZero(message = "protein은 0 이상이어야 합니다.")
    private double protein;

    @PositiveOrZero(message = "fat은 0 이상이어야 합니다.")
    private double fat;

    @PositiveOrZero(message = "sugar은 0 이상이어야 합니다.")
    private double sugar;

    @PositiveOrZero(message = "natrium은 0 이상이어야 합니다.")
    private double natrium;
}
