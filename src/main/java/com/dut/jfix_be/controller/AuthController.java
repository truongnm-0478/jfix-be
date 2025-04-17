package com.dut.jfix_be.controller;

import com.dut.jfix_be.dto.ApiResponse;
import com.dut.jfix_be.dto.request.LoginRequest;
import com.dut.jfix_be.dto.request.RefreshTokenRequest;
import com.dut.jfix_be.dto.request.UserRequest;
import com.dut.jfix_be.dto.response.AuthResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody UserRequest userRequest) {
        UserResponse authResponse = authService.register(userRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserResponse authResponse = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest,
            HttpServletRequest request
    ) {
        authService.logout(refreshTokenRequest.getRefreshToken(), request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        AuthResponse authResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(ApiResponse.success(authResponse));
    }
}