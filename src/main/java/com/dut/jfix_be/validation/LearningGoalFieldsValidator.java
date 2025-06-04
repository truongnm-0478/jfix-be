package com.dut.jfix_be.validation;

import java.time.LocalDate;

import com.dut.jfix_be.dto.request.LearningGoalRequest;
import com.dut.jfix_be.enums.JlptLevel;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LearningGoalFieldsValidator implements ConstraintValidator<LearningGoalFieldsValidation, LearningGoalRequest> {

    @Override
    public void initialize(LearningGoalFieldsValidation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LearningGoalRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        if (request.getTargetLevel() != JlptLevel.FREE) {
            if (request.getDailyMinutes() == null) {
                addConstraintViolation(context, "validation.daily.minutes.required");
                return false;
            }
            if (request.getDailyVocabTarget() == null) {
                addConstraintViolation(context, "validation.daily.vocab.required");
                return false;
            }
            if (request.getTargetDate() == null) {
                addConstraintViolation(context, "validation.target.date.required");
                return false;
            }
            
            if (request.getTargetDate().isBefore(LocalDate.now())) {
                addConstraintViolation(context, "error.target.date.must.be.future");
                return false;
            }

            if (request.getDailyMinutes() <= 0) {
                addConstraintViolation(context, "validation.daily.minutes.min");
                return false;
            }
            if (request.getDailyVocabTarget() <= 0) {
                addConstraintViolation(context, "validation.daily.vocab.min");
                return false;
            }
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
} 