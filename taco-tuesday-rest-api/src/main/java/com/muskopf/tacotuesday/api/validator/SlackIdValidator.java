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

        boolean valid;
        switch (type) {
            case New:
                valid = !employeeDAO.employeeExistsBySlackId(slackId);
                break;
            case Required:
                valid = employeeDAO.employeeExistsBySlackId(slackId);
                break;
            case Optional:
                if (isEmpty(slackId)) {
                    valid = true;
                } else {
                    valid = employeeDAO.employeeExistsBySlackId(slackId);
                }
                break;
            default:
                throw new ValidationException("Invalid Slack ID Type: " + type);
        }

        if (!valid) {
            log.warn("Slack ID \"" + slackId + "\" is not valid!");
        }

        return valid;
    }
}