package com.ssafy.foody.auth.admin.conroller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.auth.admin.dto.UpdateRoleRequest;
import com.ssafy.foody.auth.admin.service.AdminService;

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
	
	
}
