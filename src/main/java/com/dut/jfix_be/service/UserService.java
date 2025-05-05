package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserResponse;

public interface UserService {
    ChangePasswordResponse changePassword(ChangePasswordRequest request);
    UserResponse updateProfile(UserUpdateRequest request);
}
