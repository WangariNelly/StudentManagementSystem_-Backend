package com.managementsystem.studentsmanagementsystem.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateOfBirthValidator implements ConstraintValidator<BirthValidator, LocalDate> {

    private final LocalDate minDate = LocalDate.of(2000, 1, 1);
    private final LocalDate maxDate = LocalDate.of(2010, 12, 31);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return !value.isBefore(minDate) && !value.isAfter(maxDate);
    }

}
