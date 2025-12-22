package com.ssafy.foody.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.admin.dto.ActivityLevelResponse;
import com.ssafy.foody.admin.dto.UpdateActivityLevelRequest;
import com.ssafy.foody.admin.dto.UpdateRoleRequest;
import com.ssafy.foody.admin.dto.UpdateWaitingReportRequest;
import com.ssafy.foody.admin.dto.WaitingReportResponse;
import com.ssafy.foody.admin.service.AdminService;
import com.ssafy.foody.food.dto.FoodRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "관리자 전용 API")
public class AdminController {
	private final AdminService adminService;

	/**
	 * 권한 수정 (관리자만 접근 가능)
	 * PATCH /admin
	 * 요청 Body(raw)
	 * userID : 권한을 변경할 아이디
	 * role : 지정하려는 권한
	 */
	@Operation(summary = "유저 권한 수정", description = "특정 유저의 권한(ROLE)을 변경합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping
	public ResponseEntity<String> updateUserRole(
			@Valid @RequestBody UpdateRoleRequest request) {
		adminService.updateUserRole(request.getUserId(), request.getRole());
		return ResponseEntity.ok("권한이 성공적으로 수정되었습니다");

	}

	/**
	 * Foods 테이블 음식 등록 (관리자만 접근 가능)
	 * Post /admin
	 * 요청 Body(raw)
	 * code, name, category, standard, kcal, carb, protein, fat, sugar, natrium
	 */
	@Operation(summary = "음식 등록", description = "새로운 음식을 DB에 등록합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<String> addFood(@Valid @RequestBody FoodRequest request) {
		adminService.addFood(request);
		return ResponseEntity.ok("음식이 성공적으로 등록되었습니다");
	}

	/**
	 * 음식 수정 (관리자만 접근 가능)
	 * PUT /admin
	 * 요청 Body(raw)
	 * code, name, category, standard, kcal, carb, protein, fat, sugar, natrium
	 */
	@Operation(summary = "음식 수정", description = "기존 음식 정보를 수정합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/food")
	public ResponseEntity<String> updateFood(@Valid @RequestBody FoodRequest request) {
		adminService.updateFood(request);
		return ResponseEntity.ok("음식이 성공적으로 수정되었습니다");
	}

	/**
	 * 음식 삭제
	 * DELETE /admin/{code}
	 */
	@Operation(summary = "음식 삭제", description = "음식 코드로 음식을 삭제합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{code}")
	public ResponseEntity<String> deleteFood(@PathVariable String code) {
		adminService.deleteFood(code);
		return ResponseEntity.ok("음식이 성공적으로 삭제되었습니다");
	}

	/**
	 * Activity_level 테이블 value(가중치) description(설명) 수정
	 * POST /admin/activitylevel
	 * 요청 Body(raw)
	 * level, value, description
	 */
	@Operation(summary = "활동 레벨 정보 수정", description = "활동 레벨(Activity Level)의 가중치 및 설명을 수정합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/activitylevel")
	public ResponseEntity<String> updateActivityLevel(@Valid @RequestBody UpdateActivityLevelRequest request) {
		adminService.updateActivityLevelByLevel(request);
		return ResponseEntity.ok("Activity level이 성공적으로 수정되었습니다");
	}

	/**
	 * Activity level 테이블 조회
	 * GET /admin/activitylevel
	 */
	@Operation(summary = "활동 레벨 목록 조회", description = "모든 활동 레벨 정보를 조회합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/activitylevel")
	public ResponseEntity<List<ActivityLevelResponse>> findAllActivityLevels() {
		List<ActivityLevelResponse> list = adminService.findAllActivityLevels();
		log.debug("조회된 활동 레벨 : {}", list);
		// 데이터가 없으면 204 반환
		if (list == null || list.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(list);
	}

	/**
	 * Reports 테이블에서 레포트 생성 대기자 조회
	 * GET /admin/report?page={페이지수}
	 */
	@Operation(summary = "분석 대기 레포트 조회", description = "전문가 분석이 필요한 대기 중인 레포트 목록을 조회합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/report")
	public ResponseEntity<List<WaitingReportResponse>> findAllWaitingReport(
			@RequestParam(value = "page", defaultValue = "1") int page) {
		List<WaitingReportResponse> list = adminService.findAllWaitingReport(page);
		log.debug("레포트 대기 목록 : {}", list);
		return ResponseEntity.ok(list);
	}

	/**
	 * 대기중인 Reports 작성
	 * PATCH /admin/report
	 * 요청 Body(raw)
	 * id, score, character_id, comment
	 */
	@Operation(summary = "전문가 분석 작성", description = "전문가가 레포트에 대한 분석 내용(점수, 코멘트 등)을 작성합니다.")
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("/report")
	public ResponseEntity<String> updateWaitingReport(
			@AuthenticationPrincipal UserDetails userDetails,
			@Valid @RequestBody UpdateWaitingReportRequest updateReportRequest) {
		log.debug("분석 작성 전문가 id: {}", userDetails.getUsername());
		updateReportRequest.setExpertId(userDetails.getUsername());
		adminService.updateWaitingReport(updateReportRequest);
		return ResponseEntity.ok("레포트 수정이 성공적으로 완료되었습니다.");
	}

}
