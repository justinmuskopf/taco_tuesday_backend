package com.respec.tacotuesday.bl.proc;

import com.respec.tacotuesday.bl.repository.ApiKeyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyValidator {
    private Logger logger = LoggerFactory.getLogger(ApiKeyValidator.class);
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyValidator(ApiKeyRepository repository) {
        this.apiKeyRepository = repository;
    }

    public boolean isValidApiKey(String apiKey) {
        if (apiKey == null) {
            return false;
        }

        boolean keyIsValid = apiKeyRepository.existsByKey(apiKey);

        logger.info("Api Key \"" + apiKey + "\"" + (keyIsValid ? " is " : " is not ") + " valid!");

        return keyIsValid;
    }

    public boolean isInvalidApiKey(String apiKey) { return !isValidApiKey(apiKey); }
}
