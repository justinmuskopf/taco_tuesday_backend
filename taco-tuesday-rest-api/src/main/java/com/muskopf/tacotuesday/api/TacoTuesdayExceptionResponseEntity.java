package com.muskopf.tacotuesday.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

public class TacoTuesdayExceptionResponseEntity extends ResponseEntity<Object> {

    public TacoTuesdayExceptionResponseEntity() {
        super(new TacoTuesdayExceptionResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TacoTuesdayExceptionResponseEntity(HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(status), status);
    }

    public TacoTuesdayExceptionResponseEntity(String error, HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(error, status), status);
    }

    public TacoTuesdayExceptionResponseEntity(String[] errors, HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(errors, status), status);
    }

    public TacoTuesdayExceptionResponseEntity(List<String> errors, HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(errors.toArray(new String[0]), status), status);
    }

    public TacoTuesdayExceptionResponseEntity(String error, HttpStatus status, boolean retryable) {
        super(new TacoTuesdayExceptionResponse(error, status, retryable), status);
    }

    public TacoTuesdayExceptionResponseEntity(String[] errors, HttpStatus status, boolean retryable) {
        super(new TacoTuesdayExceptionResponse(errors, status, retryable), status);
    }

    public TacoTuesdayExceptionResponseEntity(List<String> errors, HttpStatus status, boolean retryable) {
        super(new TacoTuesdayExceptionResponse(errors.toArray(new String[0]), status, retryable), status);
    }

    public TacoTuesdayExceptionResponseEntity(Exception e, HttpStatus status) {
        super(new TacoTuesdayExceptionResponse(e.getMessage(), status), status);
    }
}
