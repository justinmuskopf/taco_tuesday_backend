package com.muskopf.tacotuesday.api.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class TacoTypeValidator implements ConstraintValidator<TacoType, String> {
    @Override
    public void initialize(TacoType constraintAnnotation) {}

    @Override
    public boolean isValid(String tacoType, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating taco type: " + tacoType);

        try {
            com.muskopf.tacotuesday.domain.TacoType.valueOf(tacoType);
        } catch (IllegalArgumentException e) {
            log.warn("Taco type \"" + tacoType + "\" is invalid!");
            return false;
        }

        return true;
    }
}
