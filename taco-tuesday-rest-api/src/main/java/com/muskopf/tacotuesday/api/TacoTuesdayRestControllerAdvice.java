package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class TacoTuesdayRestControllerAdvice extends ResponseEntityExceptionHandler {
    private TacoEmailer tacoEmailer;

    @Autowired
    public TacoTuesdayRestControllerAdvice(TacoEmailer tacoEmailer) {
        this.tacoEmailer = tacoEmailer;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request)
    {
        return new TacoTuesdayExceptionResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest r) {
        log.info("Handling constraint violation!");

        if (e.getConstraintViolations().contains("apiKey")) {
            log.warn("Hello?");
        }

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            errors.add(violation.getMessage());
        }

        // Get the HTTP method from the request
        HttpMethod httpMethod = ((ServletWebRequest) r).getHttpMethod();

        // Return a 404 if trying to get a specific resource, and a 400 if anything else
        HttpStatus httpStatus = httpMethod.equals(HttpMethod.GET) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        return new TacoTuesdayExceptionResponseEntity(errors, httpStatus);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.warn("Handling method argument not valid exception!");

        List<String> errors = new ArrayList<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }

        return new TacoTuesdayExceptionResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedApiKeyException.class)
    public ResponseEntity<Object> handleUnrecognizedApiKeyException(UnrecognizedApiKeyException e, WebRequest r) {
        tacoEmailer.sendExceptionEmail(e);

        return new TacoTuesdayExceptionResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchResourceException.class)
    public ResponseEntity<Object> handleNoSuchResourceExistsException(NoSuchResourceException e, WebRequest r) {
        return new TacoTuesdayExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest r) throws Exception {
        tacoEmailer.sendExceptionEmail(e);

        return handleException(e, r);
    }
}
