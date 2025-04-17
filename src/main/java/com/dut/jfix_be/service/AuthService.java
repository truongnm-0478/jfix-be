package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.UserRequest;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.dto.request.LoginRequest;
import com.dut.jfix_be.dto.request.RefreshTokenRequest;
import com.dut.jfix_be.dto.response.AuthResponse;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    UserResponse register(UserRequest userRequest);
    UserResponse login(LoginRequest loginRequest);
    void logout(String refreshToken, HttpServletRequest request);
    AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}