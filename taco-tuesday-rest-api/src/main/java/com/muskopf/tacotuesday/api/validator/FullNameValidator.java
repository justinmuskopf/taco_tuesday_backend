package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class FullNameValidator implements ConstraintValidator<FullName, String> {
    public enum FullNameType {
        Required,
        Optional
    }

    @Autowired
    private TacoTuesdayValidationContext validator;

    private FullNameType type;

    @Override
    public void initialize(FullName constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext context) {
        if (isEmpty(fullName)) {
            if (fullName == null && type == FullNameType.Optional) {
                return true;
            } else {
                validator.registerContext(ValidatorType.FullName, context, "Employee must have a full name!");
            }
        } else {
            validator.registerContext(ValidatorType.FullName, context);
        }

        return validator.validate(fullName);
    }
}