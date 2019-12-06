package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class FullNameValidator implements ConstraintValidator<FullName, String> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    @Override
    public void initialize(FullName constraintAnnotation) {}

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext context) {
        if (isEmpty(fullName)) {
            validator.registerContext(ValidatorType.FullName, context, "Employee must have a full name!");
        } else {
            validator.registerContext(ValidatorType.FullName, context);
        }

        return validator.validate(fullName);
    }
}