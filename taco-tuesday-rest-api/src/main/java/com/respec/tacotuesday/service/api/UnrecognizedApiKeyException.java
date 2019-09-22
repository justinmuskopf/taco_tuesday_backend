package com.respec.tacotuesday.service.api;

import org.springframework.http.HttpStatus;

public class UnrecognizedApiKeyError extends ApiError {
    public UnrecognizedApiKeyError(String apiKey) {
        super("Unrecognized API Key: " + apiKey, HttpStatus.BAD_REQUEST);
    }
}
