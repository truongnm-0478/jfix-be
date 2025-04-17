package com.dut.jfix_be.dto.request;

import com.dut.jfix_be.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotBlank(message = "error.username.required")
    @Size(min = 3, max = 50, message = "error.username.length")
    private String username;

    private UserRole role;

    @NotBlank(message = "error.name.required")
    @Size(min = 2, max = 100, message = "error.name.length")
    private String name;

    @Email(message = "error.email.invalid")
    @NotBlank(message = "error.email.required")
    private String email;

    @NotBlank(message = "error.phone.required")
    @Pattern(regexp = "^\\d{10,15}$", message = "invalid_phone")
    private String phone;

    private String avatar;

    @NotBlank(message = "error.password.required")
    @Size(min = 6, max = 100, message = "error.password.length")
    private String password;
}