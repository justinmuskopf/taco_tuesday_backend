package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.bl.TacoTuesdayDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyValidator {
    private Logger logger = LoggerFactory.getLogger(ApiKeyValidator.class);
    private TacoTuesdayDAO dao;

    @Autowired
    public ApiKeyValidator(TacoTuesdayDAO dao) {
        this.dao = dao;
    }

    public boolean isValidApiKey(String apiKey) {
        if (apiKey == null) {
            return false;
        }

        boolean keyIsValid = dao.apiKeyExists(apiKey);

        logger.info("Api Key \"" + apiKey + "\"" + (keyIsValid ? " is " : " is not ") + " valid!");

        return keyIsValid;
    }

    public boolean isInvalidApiKey(String apiKey) { return !isValidApiKey(apiKey); }
}
