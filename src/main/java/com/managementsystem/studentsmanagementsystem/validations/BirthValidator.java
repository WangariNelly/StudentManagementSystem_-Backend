package com.managementsystem.studentsmanagementsystem.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateOfBirthValidator.class)
public @interface BirthValidator {
    String message() default "Date of Birth must be between 1-1-2000 and 31-12-2010";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
