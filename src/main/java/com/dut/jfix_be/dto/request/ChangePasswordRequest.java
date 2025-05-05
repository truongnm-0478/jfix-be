package com.dut.jfix_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "error.password.required")
    private String oldPassword;

    @NotBlank(message = "error.password.required")
    @Size(min = 6, max = 100, message = "error.password.length")
    private String newPassword;

    @NotBlank(message = "error.password.required")
    private String confirmPassword;
} 