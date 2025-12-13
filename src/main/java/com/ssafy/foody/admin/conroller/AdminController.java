package com.ssafy.foody.admin.conroller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ssafy.foody.admin.dto.WaitingReportResponse;
import com.ssafy.foody.admin.dto.UpdateActivityLevelRequest;
import com.ssafy.foody.admin.dto.UpdateRoleRequest;
import com.ssafy.foody.admin.dto.UpdateWaitingReportRequest;
import com.ssafy.foody.admin.service.AdminService;
import com.ssafy.foody.food.dto.FoodRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;
	
	/**
	 * 권한 수정 (관리자만 접근 가능)
	 * PATCH /admin
	 * 요청 Body(raw) 
	 * userID : 권한을 변경할 아이디
	 * role : 지정하려는 권한
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping
	public ResponseEntity<String> updateUserRole(
			@RequestBody UpdateRoleRequest request) {
		adminService.updateUserRole(request.getUserId(), request.getRole());
		return ResponseEntity.ok("권한이 성공적으로 수정되었습니다");
		
	}
	
	/**
	 * Foods 테이블 음식 등록 (관리자만 접근 가능)
	 * Post /admin
	 * 요청 Body(raw)
	 * code, name, category, standard, kcal, carb, protein, fat, sugar, natrium 
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<String> addFood(@Valid @RequestBody FoodRequest request) {
		adminService.addFood(request);
		return ResponseEntity.ok("음식이 성공적으로 등록되었습니다");
	}
	
	/**
	 * 음식 삭제
	 * DELETE /admin/{code}
	 */
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
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("activitylevel")
	public ResponseEntity<String> updateActivityLevel(@Valid @RequestBody UpdateActivityLevelRequest request) {
		adminService.updateActivityLevelByLevel(request);
		return ResponseEntity.ok("Activity level이 성공적으로 수정되었습니다");
	}
	
	/**
	 * Activity level 테이블 조회
	 * GET /admin/activitylevel
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("activitylevel")
	public ResponseEntity<List<ActivityLevelResponse>> findAllActivityLevels() {
		List<ActivityLevelResponse> list = adminService.findAllActivityLevels();
		log.debug("조회된 활동 레벨 : {}", list);
		//데이터가 없으면 204 반환
		if(list == null || list.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(list);
	}
	
	/**
	 * Reports 테이블에서 레포트 생성 대기자 조회
	 * GET /admin/report?page={페이지수}
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("report")
	public ResponseEntity<List<WaitingReportResponse>> findAllWaitingReport(
			@RequestParam(value = "page", defaultValue = "1") int page) {
		List<WaitingReportResponse> list = adminService.findAllWaitingReport(page);
		log.debug("레포트 대기 목록 : {}" , list);
		//대기자가 없는 경우에는 front 에서 list size 체크해서 0일경우 대기자가 없습니다 라는 멘트 표시
		return ResponseEntity.ok(list);
	}
	
	/**
	 * 대기중인 Reports 작성
	 * PATCH /admin/report
	 * 요청 Body(raw)
	 * id, score, character_id, comment
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping("report")
	public ResponseEntity<String> updateWaitingReport(
			@Valid @RequestBody UpdateWaitingReportRequest updateReportRequest){
		adminService.updateWaitingReport(updateReportRequest);
		return ResponseEntity.ok("레포트 수정이 성공적으로 완료되었습니다.");
	}
	
	
}
