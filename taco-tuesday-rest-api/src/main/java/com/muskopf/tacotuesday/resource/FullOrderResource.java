package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@JsonTypeName("fullOrder")
@EqualsAndHashCode(callSuper = true, exclude = "individualOrders")
public class FullOrderResource extends OrderResource {
    @JsonProperty
    List<IndividualOrderResource> individualOrders;
}
