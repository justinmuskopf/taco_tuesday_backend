package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel
@Validated
@EqualsAndHashCode(callSuper = true, exclude = "individualOrders")
public class FullOrderResource extends OrderResource {
    @JsonProperty
    @NotEmpty(message = "A full order must have individual orders!")
    List<IndividualOrderResource> individualOrders;
}
