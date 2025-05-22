package com.dut.jfix_be.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {
    
    @NotBlank(message = "error.token.required")
    @Size(min = 6, max = 6, message = "error.token.length")
    private String token;
    
    @NotBlank(message = "error.password.required")
    @Size(min = 6, max = 100, message = "error.password.length")
    private String newPassword;
    
    @NotBlank(message = "error.password.confirm.required")
    private String confirmPassword;
} 