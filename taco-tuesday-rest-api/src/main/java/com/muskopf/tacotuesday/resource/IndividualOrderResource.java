package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@JsonTypeName("individualOrder")
@EqualsAndHashCode(callSuper = true)
public class IndividualOrderResource extends OrderResource {
    @JsonProperty
    EmployeeResource employee;
}
