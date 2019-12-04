package com.muskopf.tacotuesday.api.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidator.ConstraintContextHelper;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class SlackIdValidator implements ConstraintValidator<SlackId, String> {
    public enum SlackIdType {
        New,
        Optional,
        Required
    }

    @Autowired
    private TacoTuesdayValidator validator;

    private SlackIdType type;


    @Override
    public void initialize(SlackId constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String slackId, ConstraintValidatorContext context) {
        validator.registerContext(type.name() + " Slack ID", slackId, context);

        if (isEmpty(slackId)) {
            // The only time an empty Slack ID is valid is when it is optional
            if (type == SlackIdType.Optional) {
                return true;
            }


            validator.setMessage("Employee must have a Slack ID!");

//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Employee must have a Slack ID!")
//                    .addConstraintViolation();
            return false;
        }

        boolean employeeExists = validator.slackIdExists(slackId);

        // Slack ID is valid if it doesn't exist and the Employee is New, otherwise if it already exists
        boolean valid = (type == SlackIdType.New) ? !employeeExists : employeeExists;

        if (type == SlackIdType.New && employeeExists) {

        }



        if (!valid) {
            log.warn("Slack ID \"" + slackId + "\" is not valid!");
        }

        return valid;
    }


}