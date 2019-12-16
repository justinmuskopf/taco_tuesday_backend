package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.FullNameValidator.FullNameType;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FullNameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface FullName {
    FullNameType type() default FullNameType.Required;

    String message() default "Error: ${validatedValue}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
