package com.dut.jfix_be.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "username_required")
    private String username;

    @NotBlank(message = "password_required")
    private String password;
}