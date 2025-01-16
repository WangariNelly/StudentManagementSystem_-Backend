package com.managementsystem.studentsmanagementsystem.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateOfBirthValidator implements ConstraintValidator<BirthValidator, LocalDate> {
    @Override
    public void initialize(BirthValidator constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            return false;
        }
        LocalDate start = LocalDate.of(2000, 1, 1);
        LocalDate end = LocalDate.of(2010, 12, 31);
        return dob.isBefore(LocalDate.now());
//        return !value.isBefore(start) && !value.isAfter(end);
    }

}
