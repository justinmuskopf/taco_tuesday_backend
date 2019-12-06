package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.api.validator.TacoTuesdayValidationContext.ValidatorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class SlackIdValidator implements ConstraintValidator<SlackId, String> {
    public enum SlackIdType {
        New,
        Optional,
        Required
    }

    @Autowired
    private TacoTuesdayValidationContext validator;

    private SlackIdType type;

    @Override
    public void initialize(SlackId constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String slackId, ConstraintValidatorContext context) {
        // Register the context
        if (isEmpty(slackId)) {
            if (type == SlackIdType.Optional) {
                return true;
            } else {
                validator.registerContext(ValidatorType.RequiredSlackId, context, "Employee must have a Slack ID!");
            }
        } else {
            // Considers the Optional SlackIdType as Required since it is non-empty
            if (type == SlackIdType.New) {
                validator.registerContext(ValidatorType.NewSlackId, context,
                        "Invalid Slack ID (Employee already exists!): ${validatedValue}");
            } else {
                validator.registerContext(ValidatorType.RequiredSlackId, context,
                        "Invalid Slack ID (Employee does not exist!): ${validatedValue}");
            }
        }

        return validator.validate(slackId);
    }
}