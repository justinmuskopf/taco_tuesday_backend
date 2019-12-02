package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.Price;
import com.muskopf.tacotuesday.api.validator.TacoName;
import com.muskopf.tacotuesday.api.validator.TacoType;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class TacoResource {
    @JsonProperty
    @TacoType
    private String type;

    @JsonProperty
    @TacoName
    private String name;

    @JsonProperty
    @Price
    private float price;
}
