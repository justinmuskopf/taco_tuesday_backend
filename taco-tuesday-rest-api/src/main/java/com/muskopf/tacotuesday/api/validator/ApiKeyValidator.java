package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class ApiKeyValidator implements ConstraintValidator<ApiKey, String> {
    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    public void initialize(ApiKey constraintAnnotation) {}

    @Override
    public boolean isValid(String apiKey, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating API Key: " + apiKey);

        if (isEmpty(apiKey)) {
            return false;
        }

        boolean valid = employeeDAO.apiKeyExists(apiKey);
        if (!valid) {
            log.warn("API Key \"" + apiKey + "\" is not valid!");
        }

        return valid;
    }
}
