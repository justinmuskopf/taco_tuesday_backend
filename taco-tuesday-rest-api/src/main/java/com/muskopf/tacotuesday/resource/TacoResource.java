package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.Price;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class TacoResource {
    @JsonProperty
    private String type;

    @JsonProperty
    private String name;

    @JsonProperty
    @Price
    private float price;
}
