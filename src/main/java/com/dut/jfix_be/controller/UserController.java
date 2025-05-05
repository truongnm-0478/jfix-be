package com.dut.jfix_be.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<ChangePasswordResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        ChangePasswordResponse response = userService.changePassword(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
        @ModelAttribute @Valid UserUpdateRequest request
    ) {
        UserResponse response = userService.updateProfile(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/detail")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetail(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse user = userService.getUserDetail(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success(user));
    }

}