package com.muskopf.tacotuesday.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class TacoTuesdayExceptionResponseEntity extends ResponseEntity<Object> {

    public TacoTuesdayExceptionResponseEntity() {
        super(new TacoTuesdayExceptionResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TacoTuesdayExceptionResponseEntity(HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(status), status);
    }

    public TacoTuesdayExceptionResponseEntity(String message, HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(message, status), status);
    }

    public TacoTuesdayExceptionResponseEntity(String message, HttpStatus status, boolean retryable) {
        super(new TacoTuesdayExceptionResponse(message, status, retryable), status);
    }

    public TacoTuesdayExceptionResponseEntity(String message, HttpStatus status, boolean retryable, Exception cause) {
        super(new TacoTuesdayExceptionResponse(message, status, retryable, cause), status);
    }
}
