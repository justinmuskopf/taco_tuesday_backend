package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


@Slf4j
public class ApiKeyValidator implements ConstraintValidator<ApiKey, String> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    @Override
    public void initialize(ApiKey constraintAnnotation) {}

    @Override
    public boolean isValid(String apiKey, ConstraintValidatorContext context) {
        validator.registerContext(ValidatorType.ApiKey, context);
        return validator.validate(apiKey);
    }
}
