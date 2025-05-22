package com.dut.jfix_be.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPasswordRequest {
    
    @NotBlank(message = "error.email.required")
    @Email(message = "error.email.invalid")
    private String email;
} 