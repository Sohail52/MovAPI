package com.example.internintelligence_movieapidevelopment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;

public class MinSizeValidator implements ConstraintValidator<MinSize, Collection<?>> {
    private int minSize;

    @Override
    public void initialize(MinSize constraintAnnotation) {
        this.minSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Collection<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.size() >= minSize;
    }
}
