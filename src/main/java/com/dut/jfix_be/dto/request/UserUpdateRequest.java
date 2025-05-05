package com.dut.jfix_be.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "error.name.required")
    @Size(min = 2, max = 100, message = "error.name.length")
    private String name;

    @Email(message = "error.email.invalid")
    @NotBlank(message = "error.email.required")
    private String email;

    @NotBlank(message = "error.phone.required")
    @Pattern(regexp = "^\\d{10,15}$", message = "invalid_phone")
    private String phone;

    private MultipartFile avatar;
} 