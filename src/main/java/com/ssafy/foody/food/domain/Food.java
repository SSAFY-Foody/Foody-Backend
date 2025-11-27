package com.ssafy.foody.food.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Food {
	private String code; //음식 코드
	private String name; // 음식 이름
	private String category;
	private String standard;
	private double kcal; //열량
	private double carb; //탄수화물
	private double protein; //단백질
	private double fat; //지방
	private double sugar; //당
	private double natrium;//나트륨 
}
