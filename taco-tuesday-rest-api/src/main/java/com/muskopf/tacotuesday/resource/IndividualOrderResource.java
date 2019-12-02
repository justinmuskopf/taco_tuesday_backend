package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.muskopf.tacotuesday.api.validator.OrderId;
import com.muskopf.tacotuesday.api.validator.OrderIdValidator.OrderType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Data
@JsonTypeName("individualOrder")
@EqualsAndHashCode(callSuper = true)
@Validated
public class IndividualOrderResource extends OrderResource {
    @JsonProperty
    @Valid
    EmployeeResource employee;

    @JsonProperty
    @OrderId(type = OrderType.Full)
    Integer fullOrderId;
}
