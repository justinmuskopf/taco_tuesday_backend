package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.muskopf.tacotuesday.api.validator.Price;
import com.muskopf.tacotuesday.api.validator.TacoCount;
import com.muskopf.tacotuesday.api.validator.TacoMap;
import com.muskopf.tacotuesday.domain.TacoType;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@ApiModel
@Validated
public abstract class OrderResource {
    @JsonProperty
    protected Integer id;

    @JsonProperty
    @TacoMap
    private Map<TacoType, Integer> tacos;

    @JsonProperty
    @Price
    private Float total;

    @JsonProperty
    private Instant createdAt;
}
