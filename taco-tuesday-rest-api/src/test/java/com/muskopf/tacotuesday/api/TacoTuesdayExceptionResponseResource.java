package com.muskopf.tacotuesday.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class TacoTuesdayExceptionResponseResource {
    @JsonProperty
    private String[] errors;

    @JsonProperty
    private Instant occurredAt;

    @JsonProperty
    private Integer statusCode;

    @JsonProperty
    private boolean retryable;
}
