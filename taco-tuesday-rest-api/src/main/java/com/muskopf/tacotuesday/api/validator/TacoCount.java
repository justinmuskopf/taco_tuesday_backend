package com.muskopf.tacotuesday.api.validator;

import javax.validation.Payload;
import javax.validation.constraints.Min;
import java.lang.annotation.*;

@Documented
@Min(0)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface TacoCount {
    String message() default "Error: ${validatedValue}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
