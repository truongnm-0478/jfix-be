package com.dut.jfix_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.response.UserAdminResponse;
import com.dut.jfix_be.enums.UserRole;
import com.dut.jfix_be.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DataWithPageResponse<UserAdminResponse>>> getAllUsers(
        @RequestParam(value = "username", required = false) String username,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "role", required = false) UserRole role,
        @RequestParam(value = "isDeleted", required = false) Boolean isDeleted,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        DataWithPageResponse<UserAdminResponse> users = userService.getAllUsersForAdmin(username, email, role, isDeleted, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserAdminResponse>> getUserDetail(@PathVariable Integer id) {
        UserAdminResponse user = userService.getUserDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }
} 