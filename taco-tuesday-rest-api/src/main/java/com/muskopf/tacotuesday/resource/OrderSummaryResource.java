package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.domain.TacoType;
import lombok.Data;

import java.math.BigInteger;
import java.util.Map;

@Data
public class OrderSummaryResource {
    @JsonProperty
    private BigInteger individualOrderCount;
    @JsonProperty
    private BigInteger fullOrderCount;
    @JsonProperty
    private BigInteger tacoCount;
    @JsonProperty
    private Double total;
    @JsonProperty
    private Map<TacoType, BigInteger> tacos;
}
