package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.domain.TacoType;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class TacoNameValidator implements ConstraintValidator<TacoName, String> {
    @Override
    public void initialize(TacoName constraintAnnotation) {}

    @Override
    public boolean isValid(String tacoName, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating taco name: " + tacoName);

        if (isEmpty(tacoName)) {
            return false;
        }

        boolean valid = Arrays.stream(TacoType.values()).anyMatch(t -> t.getPrettyName().equals(tacoName));
        if (!valid) {
            log.warn("Taco name \"" + tacoName + "\" is not valid!");
        }

        return valid;
    }
}