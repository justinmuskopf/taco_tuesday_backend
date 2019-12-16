package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.OrderDAO;

import com.muskopf.tacotuesday.domain.TacoType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Component
@Scope("prototype")
public class TacoTuesdayValidationContext {
    /**
     * Represents a method signature used to validate a specific value
     * {@link TacoTuesdayValidationContext#ValidationMethods}
     */
    @FunctionalInterface
    private interface ValidationMethod { boolean validate(Object o); }
    private final Map<ValidatorType, ValidationMethod> ValidationMethods = new HashMap<>();

    /**
     * Represents a logging method signature
     * {@link TacoTuesdayValidationContext#ValidLogMethods}
     */
    @FunctionalInterface
    private interface LogMethod { void log(String s); }
    private final Map<Boolean, LogMethod> ValidLogMethods = new HashMap<>();

    /**
     * The types of objects that this class validates
     */
    enum ValidatorType {
        ApiKey("API Key"),
        NewSlackId("Slack ID"),
        RequiredSlackId("Slack ID"),
        IndividualOrderId("IndividualOrder ID"),
        FullOrderId("FullOrder ID"),
        FullName("Full Name"),
        TacoCount("Taco Count"),
        TacoMap("Taco Order"),
        Price("Price");

        String read;
        ValidatorType(String read) { this.read = read; };
    }

    private ValidatorType validatorType;

    @Autowired
    public TacoTuesdayValidationContext(EmployeeDAO employeeDAO, OrderDAO orderDAO) {
        ValidationMethods.put(ValidatorType.ApiKey, (Object o) -> validateString(o, employeeDAO.apiKeyExists((String) o)));
        ValidationMethods.put(ValidatorType.NewSlackId, (Object o) -> validateString(o, !employeeDAO.employeeExistsBySlackId((String) o)));
        ValidationMethods.put(ValidatorType.RequiredSlackId, (Object o) -> validateString(o, employeeDAO.employeeExistsBySlackId((String) o)));
        ValidationMethods.put(ValidatorType.IndividualOrderId, (Object o) -> validateInteger(o, orderDAO.individualOrderExistsById((Integer) o)));
        ValidationMethods.put(ValidatorType.FullOrderId, (Object o) -> validateInteger(o, orderDAO.fullOrderExistsById((Integer) o)));
        ValidationMethods.put(ValidatorType.FullName, (Object o) -> validateString(o, !isEmpty(o) && ((String) o).trim().length() > 0));
        ValidationMethods.put(ValidatorType.TacoCount, (Object o) -> validateInteger(o, ((Integer) o) >= 0));
        ValidationMethods.put(ValidatorType.Price, (Object o) -> validateFloat(o, ((Float) o) > 0));
        ValidationMethods.put(ValidatorType.TacoMap, (Object o) -> validateMap(o,
                ((Map<TacoType, Integer>) o).values().stream().allMatch(i -> validate(ValidatorType.TacoCount, i)))
        );

        ValidLogMethods.put(true, log::info);
        ValidLogMethods.put(false, log::warn);
    }

    public boolean validate(Object o) { return validate(validatorType, o); }
    private boolean validate(ValidatorType validator, Object o) {
        return ValidationMethods.get(validator).validate(o);
    }

    /**
     * Initialize this Validator
     * @param validatorType The {@code ValidatorType} that this instance will validate
     * @param context The context in which this instance is validating
     * @param messageFormat The format of the message to populate if the value is invalid
     */
    private void initialize(ValidatorType validatorType, ConstraintValidatorContext context, String messageFormat) {
        log.info("Validating " + validatorType.read);

        this.validatorType = validatorType;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageFormat).addConstraintViolation();
    }

    /**
     * A wrapper around the {@link TacoTuesdayValidationContext#initialize} method that defines a default message
     * of the format 'Invalid FIELD_NAME: "FIELD_VALUE"'
     * @param validatorType The {@code ValidatorType} that this instance will validate
     * @param context The context in which this instance is validating
     */
    public void registerContext(ValidatorType validatorType, ConstraintValidatorContext context) {
        initialize(validatorType, context, String.format("Invalid %s: ${validatedValue}", validatorType.read));
    }

    /**
     * A wrapper around the {@link TacoTuesdayValidationContext#initialize} method that allows for a custom message
     * format to be defined
     * @param validatorType The {@code ValidatorType} that this instance will validate
     * @param context The context in which this instance is validating
     */
    public void registerContext(ValidatorType validatorType, ConstraintValidatorContext context, String message) {
        initialize(validatorType, context, message);
    }

    /**
     * Log the validity of the object that was validated
     * @param validated The object that was validated
     * @param isValid The validity of the object
     * @return valid
     */
    private boolean logValidity(boolean isValid, Object validated) {
        String value = (validated == null) ? "null" : validated.toString();

        // e.g. 'Slack ID "U12345678" is invalid!'
        String validityString = String.format("%s \"%s\" is %s!", validatorType.read, value, isValid ? "is valid" : "invalid");
        ValidLogMethods.get(isValid).log(validityString);

        return isValid;
    }

    private boolean validateString(Object o, boolean valid) { return o instanceof String && !isEmpty(o) && logValidity(valid, o); }
    private boolean validateInteger(Object o, boolean valid) { return o instanceof Integer && logValidity(valid, o); }
    private boolean validateFloat(Object o, boolean valid) { return (o instanceof Float || o instanceof Double) && logValidity(valid, o); }
    private boolean validateMap(Object o, boolean valid) { return o instanceof Map && logValidity(valid, o); }
}
