package com.dut.jfix_be.service.impl;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dut.jfix_be.dto.request.LearningGoalRequest;
import com.dut.jfix_be.dto.response.LearningGoalResponse;
import com.dut.jfix_be.entity.LearningGoal;
import com.dut.jfix_be.entity.User;
import com.dut.jfix_be.enums.JlptLevel;
import com.dut.jfix_be.exception.ResourceNotFoundException;
import com.dut.jfix_be.repository.LearningGoalRepository;
import com.dut.jfix_be.repository.UserRepository;
import com.dut.jfix_be.service.LearningGoalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningGoalServiceImpl implements LearningGoalService {

    private final LearningGoalRepository learningGoalRepository;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public LearningGoalResponse createLearningGoal(LearningGoalRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
        
        // Check if user already has an active learning goal with same level
        if (hasExistingGoalWithLevel(user.getId(), request.getTargetLevel())) {
            throw new RuntimeException(
                messageSource.getMessage("error.learning.goal.exists.same.level", null, LocaleContextHolder.getLocale())
            );
        }

        LearningGoal learningGoal = LearningGoal.builder()
                .userId(user.getId())
                .targetLevel(request.getTargetLevel())
                .description(request.getDescription())
                .dailyMinutes(request.getDailyMinutes())
                .dailyVocabTarget(request.getDailyVocabTarget())
                .targetDate(request.getTargetDate())
                .createBy(user.getEmail())
                .build();

        LearningGoal savedGoal = learningGoalRepository.save(learningGoal);
        return convertToResponse(savedGoal);
    }

    @Override
    public LearningGoalResponse getLearningGoal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
        
        LearningGoal learningGoal = learningGoalRepository.findActiveGoalByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    messageSource.getMessage("error.learning.goal.not.found", null, LocaleContextHolder.getLocale())
                ));
                
        return convertToResponse(learningGoal);
    }

    @Override
    public boolean hasExistingGoal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException(
                messageSource.getMessage("error.user.not.found", new Object[]{username}, LocaleContextHolder.getLocale())
            ));
            
        return hasExistingGoalWithLevel(user.getId(), null);
    }

    private boolean hasExistingGoalWithLevel(Integer userId, JlptLevel level) {
        return learningGoalRepository.existsActiveGoalByUserIdAndLevel(userId, level);
    }

    private LearningGoalResponse convertToResponse(LearningGoal learningGoal) {
        return LearningGoalResponse.fromLearningGoal(learningGoal);
    }
} 