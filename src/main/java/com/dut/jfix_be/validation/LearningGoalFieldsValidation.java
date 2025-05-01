package com.dut.jfix_be.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LearningGoalFieldsValidator.class)
@Documented
public @interface LearningGoalFieldsValidation {
    String message() default "{validation.learning.goal.fields.required}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}