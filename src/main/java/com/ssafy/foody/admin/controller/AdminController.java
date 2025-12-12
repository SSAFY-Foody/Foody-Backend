package com.ssafy.foody.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;   
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.foody.admin.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')") // admin 권한 없이 호출할경우를 막기 위해
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUserRole(
            @PathVariable String userId) {

        adminService.updateUserRole(userId);

        return ResponseEntity.ok("권한이 성공적으로 수정되었습니다.");
    }
}
