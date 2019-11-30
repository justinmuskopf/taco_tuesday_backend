package com.muskopf.tacotuesday.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
public class TacoTuesdayExceptionResponse {
    private String message = "An unknown server error occurred!";

    private Instant occurredAt = Instant.now();

    private HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    private boolean retryable = false;

    private Exception cause = null;

    public TacoTuesdayExceptionResponse() {}

    public TacoTuesdayExceptionResponse(String message) {
        this.message = message;
    }

    public TacoTuesdayExceptionResponse(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public TacoTuesdayExceptionResponse(String message, HttpStatus statusCode) {
        this(message);
        this.statusCode = statusCode;
    }

    public TacoTuesdayExceptionResponse(String message, HttpStatus statusCode, boolean retryable) {
        this(message, statusCode);
        this.retryable = retryable;
    }

    public TacoTuesdayExceptionResponse(String message, HttpStatus statusCode, boolean retryable, Exception cause) {
        this(message, statusCode, retryable);
        this.cause = cause;
    }
}
