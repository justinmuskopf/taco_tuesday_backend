package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private float price;
}
