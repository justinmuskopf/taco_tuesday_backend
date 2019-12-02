package com.muskopf.tacotuesday.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
public class TacoTuesdayExceptionResponse {
    private String[] errors = {"An unknown server error occurred!"};

    private String occurredAt = OffsetDateTime.now(ZoneOffset.UTC).toString();

    private int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();

    private boolean retryable = false;

    public TacoTuesdayExceptionResponse() {}

    public TacoTuesdayExceptionResponse(String[] errors) {
        this.errors = errors;
    }

    public TacoTuesdayExceptionResponse(String error) {
        this(new String[]{error});
    }

    public TacoTuesdayExceptionResponse(HttpStatus statusCode) {
        this.statusCode = statusCode.value();
    }

    public TacoTuesdayExceptionResponse(String[] errors, HttpStatus statusCode) {
        this.errors = errors;
        this.statusCode = statusCode.value();
    }

    public TacoTuesdayExceptionResponse(String error, HttpStatus statusCode) {
        this(new String[]{error}, statusCode);
    }

    public TacoTuesdayExceptionResponse(String[] errors, HttpStatus statusCode, boolean retryable) {
        this(errors, statusCode);
        this.retryable = retryable;
    }

    public TacoTuesdayExceptionResponse(String error, HttpStatus statusCode, boolean retryable) {
        this(new String[]{error}, statusCode, retryable);
    }
}
