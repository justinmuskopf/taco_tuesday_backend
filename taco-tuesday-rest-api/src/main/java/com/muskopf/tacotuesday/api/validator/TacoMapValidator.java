package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.domain.TacoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

@Slf4j
public class TacoMapValidator implements ConstraintValidator<TacoMap, Map<TacoType, Integer>> {
    @Autowired
    private TacoTuesdayValidationContext validator;

    @Override
    public void initialize(TacoMap constraintAnnotation) {}

    @Override
    public boolean isValid(Map<TacoType, Integer> tacoMap, ConstraintValidatorContext context) {
        validator.registerContext(TacoTuesdayValidationContext.ValidatorType.TacoMap, context);
        return validator.validate(tacoMap);
    }
}
