package com.dut.jfix_be.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dut.jfix_be.dto.DataWithPageResponse;
import com.dut.jfix_be.dto.request.AdminUserCreateRequest;
import com.dut.jfix_be.dto.request.ChangePasswordRequest;
import com.dut.jfix_be.dto.request.UserUpdateRequest;
import com.dut.jfix_be.dto.response.ChangePasswordResponse;
import com.dut.jfix_be.dto.response.UserAdminResponse;
import com.dut.jfix_be.dto.response.UserResponse;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.CardType;
import com.dut.jfix_be.enums.UserRole;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.CardRepository;
import com.dut.jfix_be.repository.StudyLogRepository;
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
    private final StudyLogRepository studyLogRepository;
    private final CardRepository cardRepository;

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

    @Override
    @Transactional
    public String lockUser(Integer id, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{adminUsername}, LocaleContextHolder.getLocale())
                ));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found.id", new Object[]{id}, LocaleContextHolder.getLocale())
                ));
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException(messageSource.getMessage("error.cannot.lock.admin", null, LocaleContextHolder.getLocale()));
        }
        user.setDeleted(true);
        user.setUpdateBy(adminUsername);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return messageSource.getMessage("success.user.locked", new Object[]{user.getUsername()}, LocaleContextHolder.getLocale());
    }

    @Override
    @Transactional
    public String unlockUser(Integer id, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{adminUsername}, LocaleContextHolder.getLocale())
                ));
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found.id", new Object[]{id}, LocaleContextHolder.getLocale())
                ));
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException(messageSource.getMessage("error.cannot.unlock.admin", null, LocaleContextHolder.getLocale()));
        }
        user.setDeleted(false);
        user.setUpdateBy(adminUsername);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
        return messageSource.getMessage("success.user.unlocked", new Object[]{user.getUsername()}, LocaleContextHolder.getLocale());
    }

    @Override
    public Map<String, Map<String, Integer>> getLearnedCardCount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username)).getId();
        var learnedLogs = studyLogRepository.findByUserId(userId).stream()
                .filter(log -> log.getUpdateDate() != null)
                .toList();
        Map<String, Integer> learnedStats = new HashMap<>();
        Map<String, Integer> totalStats = new HashMap<>();
        learnedStats.put("vocabulary", 0);
        learnedStats.put("grammar", 0);
        learnedStats.put("paragraph", 0);
        learnedStats.put("sentence", 0);
        learnedStats.put("free_talk_topic", 0);
        learnedStats.put("speaking_question", 0);
        totalStats.put("vocabulary", 0);
        totalStats.put("grammar", 0);
        totalStats.put("paragraph", 0);
        totalStats.put("sentence", 0);
        totalStats.put("free_talk_topic", 0);
        totalStats.put("speaking_question", 0);
        // Đếm số lượng đã học
        for (var log : learnedLogs) {
            var card = cardRepository.findById(log.getCardId()).orElse(null);
            if (card == null) continue;
            CardType type = card.getType();
            switch (type) {
                case VOCABULARY -> learnedStats.put("vocabulary", learnedStats.get("vocabulary") + 1);
                case GRAMMAR -> learnedStats.put("grammar", learnedStats.get("grammar") + 1);
                case PARAGRAPH -> learnedStats.put("paragraph", learnedStats.get("paragraph") + 1);
                case SENTENCE -> learnedStats.put("sentence", learnedStats.get("sentence") + 1);
                case FREE_TALK_TOPIC -> learnedStats.put("free_talk_topic", learnedStats.get("free_talk_topic") + 1);
                case SPEAKING_QUESTION -> learnedStats.put("speaking_question", learnedStats.get("speaking_question") + 1);
                default -> {}
            }
        }
        // Đếm tổng số thẻ cần học của user theo từng loại (dựa trên study log của user)
        var allUserLogs = studyLogRepository.findByUserId(userId);
        for (var log : allUserLogs) {
            var card = cardRepository.findById(log.getCardId()).orElse(null);
            if (card == null) continue;
            CardType type = card.getType();
            switch (type) {
                case VOCABULARY -> totalStats.put("vocabulary", totalStats.get("vocabulary") + 1);
                case GRAMMAR -> totalStats.put("grammar", totalStats.get("grammar") + 1);
                case PARAGRAPH -> totalStats.put("paragraph", totalStats.get("paragraph") + 1);
                case SENTENCE -> totalStats.put("sentence", totalStats.get("sentence") + 1);
                case FREE_TALK_TOPIC -> totalStats.put("free_talk_topic", totalStats.get("free_talk_topic") + 1);
                case SPEAKING_QUESTION -> totalStats.put("speaking_question", totalStats.get("speaking_question") + 1);
                default -> {}
            }
        }
        // Gộp kết quả
        Map<String, Map<String, Integer>> result = new HashMap<>();
        for (String key : learnedStats.keySet()) {
            Map<String, Integer> value = new HashMap<>();
            value.put("learnedCount", learnedStats.get(key));
            value.put("totalCount", totalStats.get(key));
            result.put(key, value);
        }
        return result;
    }

    @Override
    @Transactional
    public UserResponse createUserByAdmin(AdminUserCreateRequest request, String adminUsername) {
        User admin = userRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageSource.getMessage("error.user.not.found", new Object[]{adminUsername}, LocaleContextHolder.getLocale())
                ));
                
        if (admin.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException(messageSource.getMessage("error.unauthorized", null, LocaleContextHolder.getLocale()));
        }
        
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.username.exists", null, LocaleContextHolder.getLocale()));
        }
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.email.exists", null, LocaleContextHolder.getLocale()));
        }
        
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new IllegalArgumentException(messageSource.getMessage("error.phone.exists", null, LocaleContextHolder.getLocale()));
        }
        
        String avatarUrl = null;
        if (request.getAvatar() != null && !request.getAvatar().isEmpty()) {
            avatarUrl = cloudinaryService.uploadImage(request.getAvatar());
        }
        
        User user = User.builder()
                .username(request.getUsername())
                .role(request.getRole())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .avatar(avatarUrl)
                .password(passwordEncoder.encode(request.getPassword()))
                .createDate(LocalDateTime.now())
                .createBy(adminUsername)
                .build();
        
        User savedUser = userRepository.save(user);
        return UserResponse.fromUser(savedUser);
    }
}
