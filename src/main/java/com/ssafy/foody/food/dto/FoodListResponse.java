package com.ssafy.foody.food.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodListResponse {
    private List<FoodResponse> content;
    private int totalCount;
    private int currentPage;
    private int totalPages;
}
