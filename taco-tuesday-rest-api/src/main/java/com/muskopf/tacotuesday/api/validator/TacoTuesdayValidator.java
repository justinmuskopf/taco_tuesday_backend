package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.OrderDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
@Scope("prototype")
public class TacoTuesdayValidator {
    private EmployeeDAO employeeDAO;
    private OrderDAO orderDAO;
    private ConstraintValidatorContext context;
    private String beingValidated;
    private boolean contextInitialized = false;

    @Autowired
    public TacoTuesdayValidator(EmployeeDAO employeeDAO, OrderDAO orderDAO) {
        this.employeeDAO = employeeDAO;
        this.orderDAO = orderDAO;
    }

    public boolean slackIdExists(String slackId) { return employeeDAO.employeeExistsBySlackId(slackId); }
    public boolean apiKeyExists(String apiKey) { return employeeDAO.apiKeyExists(apiKey); }
    public boolean fullOrderExists(Integer id) { return orderDAO.fullOrderExistsById(id); }
    public boolean individualOrderExists(Integer id) { return orderDAO.individualOrderExistsById(id); }

    public void registerContext(String field, Object value, ConstraintValidatorContext context) {
        this.beingValidated = field + ": \"" + value.toString() + "\"";
        this.context = context;
        this.contextInitialized = true;

        log.info("Validating " + beingValidated);
    }

    public void setMessage(String messageTemplate) {
        if (!contextInitialized) {
            throw new RuntimeException("Constraint Context has not been initialized!");
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
    }

    public boolean reportValid() { return report(true); }
    public boolean reportInvalid() { return report(false); }
    public boolean report(boolean valid) { return logValidity(valid); }

    public boolean logValidity(boolean valid) {
        if (valid)
            log.info(beingValidated + " is valid!");
        else
            log.warn(beingValidated + " is invalid!");

        return valid;
    }
}
