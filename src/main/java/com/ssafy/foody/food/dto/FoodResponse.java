package com.ssafy.foody.food.dto;

import com.ssafy.foody.food.domain.Food;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodInfo {
	private String name; // 음식 이름
	private String category;
	private String standard;
	private double kcal; //열량
	private double carb; //탄수화물
	private double protein; //단백질
	private double fat; //지방
	private double sugar; //당
	private double natrium;//나트륨 
	
	public FoodInfo(Food fo) {
		this.name = fo.getName();
		this.category = fo.getCategory();
		this.standard = fo.getStandard();
		this.kcal = fo.getKcal();
		this.carb = fo.getCarb();
		this.protein = fo.getProtein();
		this.fat = fo.getFat();
		this.sugar = fo.getSugar();
		this.natrium = fo.getNatrium();
	}
}
