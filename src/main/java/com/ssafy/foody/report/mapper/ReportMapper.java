package com.ssafy.foody.report.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.admin.dto.WaitingReportResponse;
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
    
    // 레포트 목록 조회
    List<Report> selectReportList(
        @Param("userId") String userId,
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
    
    // 레포트 상세 조회
    Report selectReportDetail(@Param("reportId") int reportId);
    
    // 작성자 조회 (권한 확인용)
    String findUserIdById(int id);

    // 삭제
    void deleteReport(int id);
    
    //레포트 생성 대기자 정보 조회(관리자(admin) 권한))
    List<WaitingReportResponse> findAllWaitingReport(@Param("offset") int offset, @Param("limit") int limit);
}