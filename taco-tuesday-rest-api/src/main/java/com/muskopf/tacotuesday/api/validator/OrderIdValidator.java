package com.muskopf.tacotuesday.api.validator;

import com.muskopf.tacotuesday.bl.OrderDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class OrderIdValidator implements ConstraintValidator<OrderId, Integer> {
    public enum OrderType {
        Full,
        Individual
    };

    @Autowired
    private OrderDAO orderDAO;

    private OrderType type;

    @Override
    public void initialize(OrderId constraintAnnotation) {
        type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(Integer orderId, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Validating order ID: " + orderId);
        if (isEmpty(orderId)) {
            return false;
        }

        boolean valid;
        switch (type) {
            case Full:
                valid = orderDAO.fullOrderExistsById(orderId);
                break;
            case Individual:
                valid = orderDAO.individualOrderExistsById(orderId);
                break;
            default:
                throw new ValidationException("Invalid OrderType: " + type.name());
        }

        if (!valid) {
            log.warn(type.name() + " order ID \"" + orderId + "\" is not valid!");
        }

        return valid;
    }
}