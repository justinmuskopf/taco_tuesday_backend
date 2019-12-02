package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.OrderIdValidator.OrderType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OrderIdValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface OrderId {
    OrderType type();

    String message() default "Invalid Full Order ID: ${validatedValue}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

