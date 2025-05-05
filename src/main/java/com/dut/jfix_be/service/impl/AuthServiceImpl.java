package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.config.JwtUtil;
import com.dut.jfix_be.dto.request.LoginRequest;
import com.dut.jfix_be.dto.request.RefreshTokenRequest;
import com.dut.jfix_be.dto.request.UserRequest;
import com.dut.jfix_be.dto.response.AuthResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.UserRole;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public UserResponse register(UserRequest userRequest) {
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("error.username.exists");
        }
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("error.email.exists");
        }
        if (userRepository.findByPhone(userRequest.getPhone()).isPresent()) {
            throw new IllegalArgumentException("error.phone.exists");
        }

        UserRole role = userRequest.getRole() != null ? userRequest.getRole() : UserRole.USER;
        User user = User.builder()
                .username(userRequest.getUsername())
                .role(role)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .phone(userRequest.getPhone())
                .avatar(userRequest.getAvatar())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .createDate(LocalDateTime.now())
                .createBy(userRequest.getUsername())
                .build();
        User savedUser = userRepository.save(user);

        return UserResponse.fromUser(savedUser);
    }

    @Override
    public UserResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("error.user.notfound"));

        if (user.isDeleted()) {
            throw new IllegalStateException(
                messageSource.getMessage("error.user.deleted", null, LocaleContextHolder.getLocale())
            );
        }

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return UserResponse.fromUserWithTokens(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public void logout(String refreshToken, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            User user = userRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new ResourceNotFoundException("error.invalid.refresh_token"));
            user.setRefreshToken(null);
            userRepository.save(user);
        }
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("error.invalid.refresh_token"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        if (!jwtUtil.validateRefreshToken(refreshToken, userDetails)) {
            user.setRefreshToken(null);
            userRepository.save(user);
            throw new ResourceNotFoundException("error.invalid.refresh_token");
        }
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }
}