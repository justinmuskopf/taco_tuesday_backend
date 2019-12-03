package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.muskopf.tacotuesday.api.validator.Price;
import com.muskopf.tacotuesday.api.validator.TacoCount;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Data
@Validated
public abstract class OrderResource {
    @JsonProperty
    protected Integer id;

    @JsonProperty
    @Price
    private Float total;

    @JsonProperty
    private OffsetDateTime createdAt;

    @JsonProperty
    @TacoCount
    private Integer barbacoa = 0;

    @JsonProperty
    @TacoCount
    private Integer beefFajita = 0;

    @JsonProperty
    @TacoCount
    private Integer cabeza = 0;

    @JsonProperty
    @TacoCount
    private Integer carnitas = 0;

    @JsonProperty
    @TacoCount
    private Integer chickenFajita = 0;

    @JsonProperty
    @TacoCount
    private Integer lengua = 0;

    @JsonProperty
    @TacoCount
    private Integer pastor = 0;

    @JsonProperty
    @TacoCount
    private Integer tripa = 0;
}
