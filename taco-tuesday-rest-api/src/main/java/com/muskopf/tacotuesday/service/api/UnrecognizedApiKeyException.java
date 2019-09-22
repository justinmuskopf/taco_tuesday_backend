package com.muskopf.tacotuesday.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnrecognizedApiKeyException extends RuntimeException {
    public UnrecognizedApiKeyException(String apiKey) {
        super("Unrecognized API Key: " + apiKey);
    }
}
