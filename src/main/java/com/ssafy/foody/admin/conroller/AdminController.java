package com.ssafy.foody.admin.conroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.admin.dto.UpdateRoleRequest;
import com.ssafy.foody.admin.service.AdminService;
import com.ssafy.foody.food.dto.FoodRequest;
import com.ssafy.foody.food.service.FoodService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	private final AdminService adminService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping
	public ResponseEntity<String> updateUserRole(
			@RequestBody UpdateRoleRequest request) {
		adminService.updateUserRole(request.getUserId(), request.getRole());
		return ResponseEntity.ok("권한이 성공적으로 수정되었습니다");
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<String> addFood(@Valid @RequestBody FoodRequest request) {
		adminService.addFood(request);
		return ResponseEntity.ok("음식이 성공적으로 등록되었습니다");
	}
	
}
