package com.dut.jfix_be.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.AdminUserCreateRequest;
import com.dut.jfix_be.dto.response.UserAdminResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.enums.UserRole;
import com.dut.jfix_be.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
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
    public ResponseEntity<ApiResponse<UserAdminResponse>> getUserDetail(@PathVariable Integer id) {
        UserAdminResponse user = userService.getUserDetailForAdmin(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PatchMapping("/{id}/lock")
    public ResponseEntity<ApiResponse<String>> lockUser(@PathVariable Integer id, @AuthenticationPrincipal UserDetails adminDetails) {
        String result = userService.lockUser(id, adminDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PatchMapping("/{id}/unlock")
    public ResponseEntity<ApiResponse<String>> unlockUser(@PathVariable Integer id, @AuthenticationPrincipal UserDetails adminDetails) {
        String result = userService.unlockUser(id, adminDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @ModelAttribute AdminUserCreateRequest request, @AuthenticationPrincipal UserDetails adminDetails) {
        UserResponse user = userService.createUserByAdmin(request, adminDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(user));
    }
} 