package com.dut.jfix_be.service;

import java.util.Map;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.AdminUserCreateRequest;
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
    UserAdminResponse getUserDetailForAdmin(Integer id);
    UserResponse getUserDetail(String username);
    String lockUser(Integer id, String adminUsername);
    String unlockUser(Integer id, String adminUsername);
    Map<String, Map<String, Integer>> getLearnedCardCount();
    UserResponse createUserByAdmin(AdminUserCreateRequest request, String adminUsername);
}
