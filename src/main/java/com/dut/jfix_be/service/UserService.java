package com.dut.jfix_be.service;

import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;

public interface UserService {
    ChangePasswordResponse changePassword(ChangePasswordRequest request);
}
