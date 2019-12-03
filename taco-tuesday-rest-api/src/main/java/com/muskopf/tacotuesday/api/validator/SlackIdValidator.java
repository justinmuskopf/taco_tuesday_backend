package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class SlackIdValidator implements ConstraintValidator<SlackId, String> {
    public enum SlackIdType {
        New,
        Optional,
        Required
    };

    @Autowired
    private EmployeeDAO employeeDAO;

    private SlackIdType type;

    @Override
    public void initialize(SlackId constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String slackId, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating Slack ID: " + slackId);

        boolean slackIdIsEmpty = isEmpty(slackId);
        // The only time an empty Slack ID is valid is when it is optional
        if (slackIdIsEmpty) {
            if (type == SlackIdType.Optional) {
                return true;
            }

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Employee must have a Slack ID!")
                    .addConstraintViolation();
            return false;
        }

        boolean employeeExistsBySlackId = employeeDAO.employeeExistsBySlackId(slackId);

        // Slack ID is valid if it doesn't exist and the Employee is New, otherwise if it already exists
        boolean valid = (type == SlackIdType.New) ? !employeeExistsBySlackId : employeeExistsBySlackId;
        if (!valid) {
            log.warn("Slack ID \"" + slackId + "\" is not valid!");
        }

        return valid;
    }
}