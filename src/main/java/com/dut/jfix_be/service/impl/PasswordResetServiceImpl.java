package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.request.ForgotPasswordRequest;
import com.dut.jfix_be.dto.request.ResetPasswordRequest;
import com.dut.jfix_be.dto.response.ForgotPasswordResponse;
import com.dut.jfix_be.entity.PasswordResetToken;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.PasswordResetTokenRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.EmailService;
import com.dut.jfix_be.service.PasswordResetService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    
    @Value("${app.reset-password.token.expiration-minutes:30}")
    private int tokenExpirationMinutes;
    
    private final Random random = new Random();
    
    @Override
    @Transactional
    public ForgotPasswordResponse requestPasswordReset(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.email.not.found", new Object[]{request.getEmail()}, LocaleContextHolder.getLocale())
                ));
        
        // Invalidate any existing tokens for this user
        tokenRepository.findByUserIdAndIsUsedFalse(user.getId()).ifPresent(token -> {
            token.setUsed(true);
            tokenRepository.save(token);
        });
        
        // Generate a 6-digit verification code
        String token = generateRandomCode();
        
        // Create new token
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .userId(user.getId())
                .expiryDate(LocalDateTime.now().plusMinutes(tokenExpirationMinutes))
                .createDate(LocalDateTime.now())
                .build();
        
        tokenRepository.save(resetToken);
        
        // Send email with reset token
        emailService.sendPasswordResetEmail(user, token);
        
        return ForgotPasswordResponse.builder()
                .message(messageSource.getMessage("success.password.reset.email.sent", null, LocaleContextHolder.getLocale()))
                .build();
    }
    
    @Override
    @Transactional
    public String resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(
                    messageSource.getMessage("error.password.confirm.not.match", null, LocaleContextHolder.getLocale())
            );
        }
        
        PasswordResetToken resetToken = tokenRepository.findByTokenAndIsUsedFalse(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.token.invalid", null, LocaleContextHolder.getLocale())
                ));
        
        if (resetToken.isExpired()) {
            throw new IllegalStateException(
                    messageSource.getMessage("error.token.expired", null, LocaleContextHolder.getLocale())
            );
        }
        
        User user = userRepository.findById(resetToken.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", null, LocaleContextHolder.getLocale())
                ));
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateDate(LocalDateTime.now());
        user.setUpdateBy(user.getUsername());
        user.setRefreshToken(null);
        
        resetToken.setUsed(true);
        
        userRepository.save(user);
        tokenRepository.save(resetToken);
        
        return messageSource.getMessage("success.password.reset", null, LocaleContextHolder.getLocale());
    }
    
    private String generateRandomCode() {
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
} 