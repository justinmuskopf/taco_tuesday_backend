package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class OrderIdValidator implements ConstraintValidator<OrderId, Integer> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    public enum OrderType {
        Full,
        Individual
    }

    private OrderType type;

    @Override
    public void initialize(OrderId constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Integer orderId, ConstraintValidatorContext context) {
        ValidatorType validatorType = (type == OrderType.Full) ? ValidatorType.FullOrderId : ValidatorType.IndividualOrderId;
        validator.registerContext(validatorType, context);

        return validator.validate(orderId);
    }
}