package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.CloudinaryService;
import com.dut.jfix_be.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
                ));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage("error.password.old.incorrect", null, LocaleContextHolder.getLocale()));
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage("error.password.confirm.not.match", null, LocaleContextHolder.getLocale()));
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException(messageSource.getMessage("error.password.new.same_as_old", null, LocaleContextHolder.getLocale()));
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdateDate(LocalDateTime.now());
        user.setUpdateBy(username);
        user.setRefreshToken(null);
        userRepository.save(user);
        String message = messageSource.getMessage("success.password.changed", null, LocaleContextHolder.getLocale());
        return ChangePasswordResponse.builder().message(message).build();
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UserUpdateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
                ));

        userRepository.findByEmail(request.getEmail())
                .filter(u -> !u.getUsername().equals(username))
                .ifPresent(u -> { throw new IllegalArgumentException("error.email.exists"); });
        userRepository.findByPhone(request.getPhone())
                .filter(u -> !u.getUsername().equals(username))
                .ifPresent(u -> { throw new IllegalArgumentException("error.phone.exists"); });
        String oldAvatar = user.getAvatar();
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            if (oldAvatar != null && !oldAvatar.isEmpty()) {
                cloudinaryService.deleteImageByUrl(oldAvatar);
            }
            String avatarUrl = cloudinaryService.uploadImage(request.getAvatar());
            user.setAvatar(avatarUrl);
        }
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUpdateDate(LocalDateTime.now());
        user.setUpdateBy(username);
        userRepository.save(user);
        return UserResponse.fromUser(user);
    }
}
