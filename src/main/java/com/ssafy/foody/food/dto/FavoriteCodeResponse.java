package com.ssafy.foody.food.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCodeResponse {
    private Long favoriteId;
    private String foodCode;
    private Integer userFoodCode;
}
