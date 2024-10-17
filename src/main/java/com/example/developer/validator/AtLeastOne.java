package com.example.developer.validator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = AtLeastOneValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AtLeastOne {
    String message() default "Either file or gitRepoUrl must be provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] fields();
}
