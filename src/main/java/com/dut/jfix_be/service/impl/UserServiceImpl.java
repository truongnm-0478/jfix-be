package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserAdminResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.UserRole;
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

    @Override
    public DataWithPageResponse<UserAdminResponse> getAllUsersForAdmin(String username, String email, UserRole role, Boolean isDeleted, int page, int size, String sortBy, String sortDir) {
        List<User> filtered = userRepository.findAll().stream()
                .filter(u -> username == null || u.getUsername().toLowerCase().contains(username.toLowerCase()))
                .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(u -> role == null || u.getRole() == role)
                .filter(u -> isDeleted == null || u.isDeleted() == isDeleted)
                .sorted((u1, u2) -> {
                    Comparator<User> comparator;
                    switch (sortBy) {
                        case "username": comparator = Comparator.comparing(User::getUsername, String.CASE_INSENSITIVE_ORDER); break;
                        case "email": comparator = Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER); break;
                        case "name": comparator = Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER); break;
                        case "role": comparator = Comparator.comparing(u -> u.getRole().name()); break;
                        case "isDeleted": comparator = Comparator.comparing(User::isDeleted); break;
                        case "id": default: comparator = Comparator.comparing(User::getId); break;
                    }
                    return "desc".equalsIgnoreCase(sortDir) ? comparator.reversed().compare(u1, u2) : comparator.compare(u1, u2);
                })
                .collect(Collectors.toList());
        int total = filtered.size();
        int totalPages = (int) Math.ceil((double) total / size);
        int fromIndex = Math.max(0, page * size);
        int toIndex = Math.min(filtered.size(), fromIndex + size);
        List<UserAdminResponse> pageData = (fromIndex > toIndex) ? List.of() : filtered.subList(fromIndex, toIndex).stream()
                .map(UserAdminResponse::fromUser)
                .collect(Collectors.toList());
        Integer nextPage = (page + 1 < totalPages) ? page + 1 : null;
        Integer previousPage = (page > 0) ? page - 1 : null;
        return DataWithPageResponse.<UserAdminResponse>builder()
                .data(pageData)
                .totalRecords(total)
                .totalPages(totalPages)
                .nextPage(nextPage)
                .previousPage(previousPage)
                .build();
    }

    @Override
    public UserAdminResponse getUserDetailForAdmin(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found.id", new Object[]{id}, LocaleContextHolder.getLocale())
                ));
        return UserAdminResponse.fromUser(user);
    }

    @Override
    public UserResponse getUserDetail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
                ));
        return UserResponse.fromUser(user);
    }
}
