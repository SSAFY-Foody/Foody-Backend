package com.ssafy.foody.report.mapper;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.foody.admin.dto.UpdateWaitingReportRequest;
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
            @Param("endDate") LocalDate endDate);

    // 레포트 전체 개수 조회 (페이지네이션용)
    int selectReportCount(
            @Param("userId") String userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 레포트 상세 조회
    Report selectReportDetail(@Param("reportId") int reportId);

    // 작성자 조회 (권한 확인용)
    String findUserIdById(int id);

    // 삭제
    void deleteReport(int id);

    // 레포트 생성 대기자 정보 조회(관리자(admin) 권한))
    List<WaitingReportResponse> findAllWaitingReport(@Param("offset") int offset, @Param("limit") int limit);

    // 대기중인 레포트 작성(관리자(admin) 권한))
    int updateWaitingReport(UpdateWaitingReportRequest updateReportRequest);

    // 수정한 레포트 대기상태 변경 (is_waited = false)
    void toggleFalseWaitingStatus(int id);

    // 공유 상태 변경
    void updateIsShared(@Param("reportId") int reportId, @Param("isShared") boolean isShared);

    // 공유된 레포트 목록 조회
    List<Report> selectSharedReports(@Param("offset") int offset, @Param("limit") int limit);
    
    // 공유된 레포트 개수 조회
    int selectSharedReportCount();

    // 댓글 등록
    void insertComment(com.ssafy.foody.report.dto.ReportComment comment);

    // 댓글 조회
    List<com.ssafy.foody.report.dto.ReportComment> selectComments(int reportId);

    // 댓글 삭제
    void deleteComment(int commentId);
    
    // 댓글 작성자 확인
    String findCommentAuthorById(int commentId);
}