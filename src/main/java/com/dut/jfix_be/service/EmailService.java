package com.dut.jfix_be.service;

import com.dut.jfix_be.entity.User;

public interface EmailService {
    void sendPasswordResetEmail(User user, String resetToken);
} 