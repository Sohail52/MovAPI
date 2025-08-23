package com.example.internintelligence_movieapidevelopment.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinSizeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinSize {
    String message() default "Size must be at least {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}
