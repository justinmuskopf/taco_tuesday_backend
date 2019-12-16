package com.muskopf.tacotuesday.api.validator;


import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, Float> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    @Override
    public void initialize(Price constraintAnnotation) {}

    @Override
    public boolean isValid(Float aFloat, ConstraintValidatorContext context) {
        validator.registerContext(ValidatorType.Price, context);
        return validator.validate(aFloat);
    }
}
