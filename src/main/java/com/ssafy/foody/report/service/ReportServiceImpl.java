package com.ssafy.foody.report.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.foody.food.domain.Food;
import com.ssafy.foody.food.domain.UserFood;
import com.ssafy.foody.food.mapper.FoodMapper;
import com.ssafy.foody.report.domain.Meal;
import com.ssafy.foody.report.domain.MealFood;
import com.ssafy.foody.report.domain.Report;
import com.ssafy.foody.report.dto.AiReportRequest;
import com.ssafy.foody.report.dto.AiReportResponse;
import com.ssafy.foody.report.dto.ReportRequest;
import com.ssafy.foody.report.mapper.ReportMapper;
import com.ssafy.foody.user.domain.StdInfo;
import com.ssafy.foody.user.domain.User;
import com.ssafy.foody.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final UserMapper userMapper;
	private final FoodMapper foodMapper;
	private final ReportMapper reportMapper;
	private final AiReportService aiReportService; // 순환 참조 주의
	
	// 한 페이지당 보여줄 개수 정의
	private static final int LIST_LIMIT = 10;

	@Override
	@Transactional
	public void createReport(String userId, ReportRequest request) {
		// 유저 표준 정보(StdInfo) 가져오기
		User user = userMapper.findUserWithStdInfo(userId);

		// 방어 코드, 유저 정보나 표준 정보 없으면 에러
		if (user == null) {
			throw new IllegalArgumentException("회원 정보가 없습니다.");
		} else if (user.getStdInfo() == null) {
			throw new IllegalArgumentException("표준 영양소 정보가 없습니다. 회원 정보를 먼저 수정해주세요.");
		}
		
		StdInfo std = user.getStdInfo();
		
		log.debug("User: {}, StdInfo: {}", user, std);

		// 분석 요청이 null일 경우 기본 false로 처리
		boolean isWaitedValue = (request.getIsWaited() != null) ? request.getIsWaited() : false;

		// 레포트 껍데기 생성 (일단 저장해서 ID 확보)
		Report report = Report.builder().userId(userId)
				// 유저 정보
				.userAge(user.getAge()).userHeight(user.getHeight()).userWeight(user.getWeight())
				.userGender(user.getGender()).userActivityLevel(user.getActivityLevel())
				// 표준 정보
				.userStdWeight(std.getStdWeight()).userStdKcal(std.getStdKcal()).userStdCarb(std.getStdCarb())
				.userStdProtein(std.getStdProtein()).userStdFat(std.getStdFat()).userStdSugar(std.getStdSugar())
				.userStdNatrium(std.getStdNatrium()).isWaited(isWaitedValue).build();
		reportMapper.saveReport(report); // report_id 생성
		
		// 하루 전체 영양소 합계 변수
		double dayTotalKcal = 0;
		double dayTotalCarb = 0;
		double dayTotalProtein = 0;
		double dayTotalFat = 0;
		double dayTotalSugar = 0;
		double dayTotalNatrium = 0;
		
		// [AI 요청용] 끼니별 정보 준비
        List<AiReportRequest.MealInfo> mealInfos = new ArrayList<>();

		// 끼니(Meals) 반복 처리
		if (request.getMeals() != null) {
			for (ReportRequest.MealRequest mealReq : request.getMeals()) {

				// 끼니별 영양소 합계 변수
				double mealTotalKcal = 0;
				double mealTotalCarb = 0;
				double mealTotalProtein = 0;
				double mealTotalFat = 0;
				double mealTotalSugar = 0;
				double mealTotalNatrium = 0;

				// Meal 껍데기 저장 (ID 확보)
				Meal meal = Meal.builder().reportId(report.getId()).mealType(mealReq.getMealType()).build();
				reportMapper.saveMeal(meal); // meal_id 생성됨
				
				// [AI 요청용] 음식 리스트 준비
                List<AiReportRequest.FoodInfo> foodInfos = new ArrayList<>();

				// 음식(Foods) 반복 처리
				if (mealReq.getFoods() != null) {
					for (ReportRequest.FoodItem item : mealReq.getFoods()) {

						String dbFoodCode = null;
						Integer userFoodCode = null;

						// 계산 기준값 (기준량)
						double baseStandard = 0;
						double baseKcal = 0;
						double baseCarb = 0;
						double baseProtein = 0;
						double baseFat = 0;
						double baseSugar = 0;
						double baseNatrium = 0;

						// case 1: DB 음식인 경우 (foodCode 있음)
						if (item.getFoodCode() != null && !item.getFoodCode().isEmpty()) {
							dbFoodCode = item.getFoodCode();
							Food dbFood = foodMapper.findFoodByCode(dbFoodCode);

							// DB 정보로 item 채우기 (영양소 정보 덮어쓰기)
							if (dbFood != null) {
								item.setName(dbFood.getName());
								// 문자열("100g") 파싱 -> 숫자(100.0)
								baseStandard = parseStandard(dbFood.getStandard());

								baseKcal = dbFood.getKcal();
								baseCarb = dbFood.getCarb();
								baseProtein = dbFood.getProtein();
								baseFat = dbFood.getFat();
								baseSugar = dbFood.getSugar();
								baseNatrium = dbFood.getNatrium();
							}
						}
						// case 2: 사용자 입력 / AI 분석 이미지 음식 (foodCode 없음) -> user_foods에 저장
						else {
							// 사용자가 입력한 기준 중량 사용 (없으면 100g 가정)
							baseStandard = (item.getStandard() != null && item.getStandard() > 0) ? item.getStandard()
									: 100.0;

							baseKcal = item.getKcal() != null ? item.getKcal() : 0;
							baseCarb = item.getCarb() != null ? item.getCarb() : 0;
							baseProtein = item.getProtein() != null ? item.getProtein() : 0;
							baseFat = item.getFat() != null ? item.getFat() : 0;
							baseSugar = item.getSugar() != null ? item.getSugar() : 0;
							baseNatrium = item.getNatrium() != null ? item.getNatrium() : 0;

							// UserFood 저장 (사용자 정의 음식 등록)
							UserFood customFood = UserFood.builder().registUserId(userId).name(item.getName())
									.standard(String.valueOf(baseStandard))

									.kcal(baseKcal).carb(baseCarb).protein(baseProtein).fat(baseFat).sugar(baseSugar)
									.natrium(baseNatrium)

									.build();

							foodMapper.saveUserFood(customFood);
							userFoodCode = customFood.getCode();
						}

						// 비례식 계산: (먹은 양 / 기준 양) * 영양소
						// item.getWeight()는 사용자가 실제 먹은 양(g 또는 ml)
						double ratio = item.getEatenWeight() / baseStandard;

                        double eatenKcal = roundTwo(baseKcal * ratio);
                        double eatenCarb = roundTwo(baseCarb * ratio);
                        double eatenProtein = roundTwo(baseProtein * ratio);
                        double eatenFat = roundTwo(baseFat * ratio);
                        double eatenSugar = roundTwo(baseSugar * ratio);
                        double eatenNatrium = roundTwo(baseNatrium * ratio);

						// 합계 누적
						mealTotalKcal += eatenKcal;
						mealTotalCarb += eatenCarb;
						mealTotalProtein += eatenProtein;
						mealTotalFat += eatenFat;
						mealTotalSugar += eatenSugar;
						mealTotalNatrium += eatenNatrium;

						// MealFood 저장
						MealFood mealFood = MealFood.builder().mealId(meal.getId()).foodCode(dbFoodCode)
								.userFoodCode(userFoodCode).eatenWeight(item.getEatenWeight())
								.build();
						reportMapper.saveMealFood(mealFood);
						
						// [AI 요청용] 음식 객체 추가
                        foodInfos.add(AiReportRequest.FoodInfo.builder()
                                .name(item.getName())
                                .eatenWeight(item.getEatenWeight())
                                .eatenKcal(eatenKcal)
                                .eatenCarb(eatenCarb)
                                .eatenProtein(eatenProtein)
                                .eatenFat(eatenFat)
                                .eatenSugar(eatenSugar)
                                .eatenNatrium(eatenNatrium)
                                .build());
					}
				} // end food loop

				// 끼니(Meal) 총합 업데이트
				meal.setTotalKcal(roundTwo(mealTotalKcal));
                meal.setTotalCarb(roundTwo(mealTotalCarb));
                meal.setTotalProtein(roundTwo(mealTotalProtein));
                meal.setTotalFat(roundTwo(mealTotalFat));
                meal.setTotalSugar(roundTwo(mealTotalSugar));
                meal.setTotalNatrium(roundTwo(mealTotalNatrium));

				// Meal 업데이트
				reportMapper.updateMeal(meal);

				// 하루 전체 합계 누적
				dayTotalKcal += mealTotalKcal;
				dayTotalCarb += mealTotalCarb;
				dayTotalProtein += mealTotalProtein;
				dayTotalFat += mealTotalFat;
				dayTotalSugar += mealTotalSugar;
				dayTotalNatrium += mealTotalNatrium;
				
				// [AI 요청용] 끼니 객체 추가
                mealInfos.add(AiReportRequest.MealInfo.builder()
                        .mealType(mealReq.getMealType())
                        .totalKcal(roundTwo(mealTotalKcal))
                        .totalCarb(roundTwo(mealTotalCarb))
                        .totalProtein(roundTwo(mealTotalProtein))
                        .totalFat(roundTwo(mealTotalFat))
                        .totalSugar(roundTwo(mealTotalSugar))
                        .totalNatrium(roundTwo(mealTotalNatrium))
                        .foods(foodInfos) // 위에서 만든 음식 리스트 포함
                        .build());

			} // end meal loop
		}

		Integer characterId = null;
		Double score = null;
		String comment = null;

		// 분석 대기가 아닐 때만 AI 요청
        if (!request.getIsWaited()) {
            try {
                // AI 요청 DTO 생성
                AiReportRequest aiRequest = AiReportRequest.builder()
                        .stdInfo(std)
                        // 활동 레벨 설명
                        .userActivityLevelDesc(userMapper.getActivityDesc(user.getActivityLevel()))
                        
                        // 하루 총합
                        .dayTotalKcal(roundTwo(dayTotalKcal))
                        .dayTotalCarb(roundTwo(dayTotalCarb))
                        .dayTotalProtein(roundTwo(dayTotalProtein))
                        .dayTotalFat(roundTwo(dayTotalFat))
                        .dayTotalSugar(roundTwo(dayTotalSugar))
                        .dayTotalNatrium(roundTwo(dayTotalNatrium))
                        
                        // 끼니 리스트 (음식 포함)
                        .meals(mealInfos)
                        .build();

                // AI 서비스 호출
                AiReportResponse aiResponse = aiReportService.analyzeMeal(aiRequest);

                if (aiResponse != null) {
                    characterId = aiResponse.getCharacterId();
                    score = aiResponse.getScore();
                    comment = aiResponse.getComment();
                } else {
                    log.error("AI 분석 응답 NULL - userId: {}", userId);
                    throw new RuntimeException("AI 분석 서버로부터 유효한 응답을 받지 못했습니다.");
                }

            } catch (Exception e) {
            	log.error("AI 레포트 분석 요청 실패", e);
                throw new RuntimeException("AI 분석 중 오류가 발생하여 레포트 생성을 취소합니다.", e);
            }
        }

		// 레포트 최종 업데이트
		report.setTotalKcal(roundTwo(dayTotalKcal));
        report.setTotalCarb(roundTwo(dayTotalCarb));
        report.setTotalProtein(roundTwo(dayTotalProtein));
        report.setTotalFat(roundTwo(dayTotalFat));
        report.setTotalSugar(roundTwo(dayTotalSugar));
        report.setTotalNatrium(roundTwo(dayTotalNatrium));

		report.setScore(score);
		report.setComment(comment);
		report.setCharacterId(characterId);

		reportMapper.updateReportResult(report); // 최종 저장
	}
	
	@Override
    @Transactional(readOnly = true) // 조회 전용
    public List<Report> getReportList(String userId, int page, LocalDate startDate, LocalDate endDate) {
		int offset = (page - 1) * LIST_LIMIT;
		
        return reportMapper.selectReportList(userId, offset, LIST_LIMIT, startDate, endDate);
    }
	
	@Override
    @Transactional(readOnly = true)
    public Report getReportDetail(String userId, int reportId) {
        // 레포트 정보 가져오기
        Report report = reportMapper.selectReportDetail(reportId);

        // 레포트가 존재하는지 확인
        if (report == null) {
            throw new IllegalArgumentException("해당 레포트를 찾을 수 없습니다.");
        }

        // 내 리포트가 맞는지 확인 (IDOR 방지)
        // IDOR (Insecure Direct Object References, 부적절한 인가)
        if (!report.getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 레포트에 대한 접근 권한이 없습니다.");
        }

        return report;
    }
	
	@Override
    @Transactional
    public void deleteReport(String userId, int reportId) {
        // 레포트 생성 유저 확인
        String reportUserId = reportMapper.findUserIdById(reportId);
        
        if (reportUserId == null) {
            throw new IllegalArgumentException("해당 레포트가 존재하지 않습니다.");
        }

        // 소유자 확인 (내 리포트만 삭제 가능)
        if (!reportUserId.equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        // 삭제 실행
        reportMapper.deleteReport(reportId);
    }

	// 헬퍼 메서드: DB의 standard 문자열("100g", "200ml")에서 숫자만 추출
	private double parseStandard(String standard) {
		if (standard == null)
			return 100.0; // 기본값
		try {
			// 숫자가 아닌 문자 제거 후 파싱
			String numStr = standard.replaceAll("[^0-9.]", "");
			return numStr.isEmpty() ? 100.0 : Double.parseDouble(numStr);
		} catch (NumberFormatException e) {
			return 100.0;
		}
	}
	
	// 소수점 2자리까지 나오게끔 반올림 메서드
    private double roundTwo(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
