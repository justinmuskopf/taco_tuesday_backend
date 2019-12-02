package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class SlackIdValidator implements ConstraintValidator<SlackId, String> {
    @Autowired
    private EmployeeDAO employeeDAO;

    private boolean required;

    @Override
    public void initialize(SlackId constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String slackId, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating Slack ID: " + slackId);
        if (isEmpty(slackId)) {
            return !required;
        }

        boolean valid = employeeDAO.employeeExistsBySlackId(slackId);
        if (!valid) {
            log.warn("Slack ID \"" + slackId + "\" is not valid!");
        }

        return valid;
    }
}