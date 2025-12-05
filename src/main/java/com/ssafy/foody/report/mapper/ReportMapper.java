package com.ssafy.foody.report.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.foody.report.domain.Meal;
import com.ssafy.foody.report.domain.MealFood;
import com.ssafy.foody.report.domain.Report;

@Mapper
public interface ReportMapper {
    
    // 리포트 껍데기 저장 (ID 반환용)
    void saveReport(Report report);

    // 끼니 저장
    void saveMeal(Meal meal);

    // 상세 음식 저장
    void saveMealFood(MealFood mealFood);

    // 리포트 최종 업데이트 (총칼로리, 점수, 캐릭터 등)
    void updateReportResult(Report report);
    
    // 끼니 업데이트
    void updateMeal(Meal meal);
}