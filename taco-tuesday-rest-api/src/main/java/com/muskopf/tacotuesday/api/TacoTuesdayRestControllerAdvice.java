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
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.xml.ws.Response;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        log.info(endpoint(request) + "Handling constraint violation!");

        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            // Find out if this violation is due to an API key
            String[] split = violation.getPropertyPath().toString().split("\\.");
            if (split[split.length - 1].equals("apiKey")) {
                return handleUnrecognizedApiKeyException(new UnrecognizedApiKeyException(violation.getInvalidValue().toString()), request);
            }

            errors.add(violation.getMessage());
        }

        // Get the HTTP method from the request
        HttpMethod httpMethod = ((ServletWebRequest) request).getHttpMethod();

        // Return a 404 if trying to get a specific resource, and a 400 if anything else
        HttpStatus httpStatus = httpMethod.equals(HttpMethod.GET) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

        return new TacoTuesdayExceptionResponseEntity(errors, httpStatus);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
                                                               HttpStatus status, WebRequest r)
    {
        log.warn(endpoint(r) + "Method Argument Not Valid: " + e.getParameter().toString());

        List<String> errors = new ArrayList<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errors.add(fieldError.getDefaultMessage());
        }

        return new TacoTuesdayExceptionResponseEntity(errors, fieldErrors, HttpStatus.BAD_REQUEST, false);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest r) {
        log.warn(endpoint(r) + "Invalid Method Argument Type: " + e.getMessage());

        String errorString = String.format("%s must be of type %s!", e.getName(), e.getRequiredType().getSimpleName());

        return new TacoTuesdayExceptionResponseEntity(errorString, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException e,
                                                                       HttpHeaders headers, HttpStatus status, WebRequest r)
    {
        String parameterName = e.getParameterName();
        log.warn(endpoint(r) + "Missing " + parameterName);

        if (parameterName.equals("apiKey")) {
            return new TacoTuesdayExceptionResponseEntity("No API Key Provided!", HttpStatus.UNAUTHORIZED);
        }

        return new TacoTuesdayExceptionResponseEntity("Missing required parameter: " + e.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnrecognizedApiKeyException.class)
    public ResponseEntity<Object> handleUnrecognizedApiKeyException(UnrecognizedApiKeyException e, WebRequest r) {
        log.warn(endpoint(r) + e.getMessage());

        tacoEmailer.sendExceptionEmail(e);

        return new TacoTuesdayExceptionResponseEntity(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchResourceException.class)
    public ResponseEntity<Object> handleNoSuchResourceExistsException(NoSuchResourceException e, WebRequest r) {
        log.warn(endpoint(r) + "No Such Resource: " + e.getMessage());

        return new TacoTuesdayExceptionResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUnrecognizedExceptions(Exception e, WebRequest r) throws Exception {
        log.warn(endpoint(r) + "Handling unrecognized exception (" + e.getClass().getSimpleName() + "): " + e.getMessage());

        tacoEmailer.sendExceptionEmail(e);

        return new TacoTuesdayExceptionResponseEntity("An unknown Exception occurred!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String endpoint(WebRequest r) {
        return "[" + ((ServletWebRequest) r).getRequest().getRequestURI() + "] ";
    }
}
