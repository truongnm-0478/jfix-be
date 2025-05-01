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

        // If targetLevel is FREE, all other fields can be null
        if (request.getTargetLevel() == JlptLevel.FREE) {
            return true;
        }

        // If targetLevel is not FREE, validate other required fields
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
        
        // Validate that target date is in the future
        if (request.getTargetDate().isBefore(LocalDate.now())) {
            addConstraintViolation(context, "error.target.date.must.be.future");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
} 