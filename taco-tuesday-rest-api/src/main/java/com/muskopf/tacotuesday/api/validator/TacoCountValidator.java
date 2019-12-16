package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class TacoCountValidator implements ConstraintValidator<TacoCount, Integer> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    @Override
    public void initialize(TacoCount constraintAnnotation) {}

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext context) {
        validator.registerContext(ValidatorType.TacoCount, context);
        return validator.validate(integer);
    }
}
