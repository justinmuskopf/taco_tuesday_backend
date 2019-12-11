package com.muskopf.tacotuesday.api.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface Price {
    String message() default "Error: ${validatedValue}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
