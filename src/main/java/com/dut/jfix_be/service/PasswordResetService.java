package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.ForgotPasswordRequest;
import com.dut.jfix_be.dto.request.ResetPasswordRequest;
import com.dut.jfix_be.dto.response.ForgotPasswordResponse;

public interface PasswordResetService {
    ForgotPasswordResponse requestPasswordReset(ForgotPasswordRequest request);
    String resetPassword(ResetPasswordRequest request);
} 