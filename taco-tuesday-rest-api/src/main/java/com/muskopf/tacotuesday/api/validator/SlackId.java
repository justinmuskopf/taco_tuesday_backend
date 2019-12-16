package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.SlackIdValidator.SlackIdType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlackIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface SlackId {
    SlackIdType type() default SlackIdType.Required;

    String message() default "Error: ${validatedValue}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
