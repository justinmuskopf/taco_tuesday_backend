package com.muskopf.tacotuesday.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UnrecognizedApiKeyException extends RuntimeException {
    public UnrecognizedApiKeyException(String apiKey) {
        super("Unrecognized API Key: " + apiKey);
    }
}
