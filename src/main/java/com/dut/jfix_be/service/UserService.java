package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserAdminResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.enums.UserRole;

public interface UserService {
    ChangePasswordResponse changePassword(ChangePasswordRequest request);
    UserResponse updateProfile(UserUpdateRequest request);
    DataWithPageResponse<UserAdminResponse> getAllUsersForAdmin(String username, String email, UserRole role, Boolean isDeleted, int page, int size, String sortBy, String sortDir);
}
