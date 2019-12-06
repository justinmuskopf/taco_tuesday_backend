package com.muskopf.tacotuesday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muskopf.tacotuesday.api.TacoTuesdayExceptionResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@ActiveProfiles("test")
public class TacoTuesdayApiHelper {
    private static final String TT_API_BASE_URL = "/taco-tuesday/v1";
    private static final String API_KEY = UUID.randomUUID().toString();
    private static final String INVALID_API_KEY = "superInvalidApiKey";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public TacoTuesdayApiHelper(MockMvc mockMvc, ObjectMapper objectMapper,
                                TacoTuesdayPersistenceInitializer persistenceInitializer)
    {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;

        persistenceInitializer.persistMockApiKey(API_KEY);
    }

    public enum ApiKeyStatus {
        VALID,
        INVALID,
        EMPTY
    }

    /**
     * Convenience ENUM to represent HTTP statuses
     */
    public enum ResponseStatus {
        OK(status().isOk()),
        CREATED(status().isCreated()),
        BAD_REQUEST(status().isBadRequest()),
        NOT_FOUND(status().isNotFound()),
        UNAUTHORIZED(status().isUnauthorized());

        ResultMatcher status;

        ResponseStatus(ResultMatcher status) {
            this.status = status;
        }
    }

    /**
     * Perform an HTTP operation against the TT API using Mock MVC
     *
     * @param request        The request to perform
     * @param responseStatus The status to expect from the response
     * @param apiKeyStatus   The type of API key to use
     * @return The result of the operation
     */
    private MvcResult performMvcOperation(MockHttpServletRequestBuilder request, ResponseStatus responseStatus, ApiKeyStatus apiKeyStatus)
    {
        if (apiKeyStatus == ApiKeyStatus.VALID) {
            request.param("apiKey", API_KEY);
        } else if (apiKeyStatus == ApiKeyStatus.INVALID) {
            request.param("apiKey", INVALID_API_KEY);
        }

        // Perform request
        try {
            return mockMvc.perform(request)
                    .andExpect(responseStatus.status)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException("Error performing MockMvc operation: " + request.toString(), e);
        }
    }

    /**
     * Perform a GET operation on the TT API at {@code path}
     *
     * @param path           The path to perform the GET on (e.g. "/employees")
     * @param responseStatus The HTTP status expected from the response
     * @param apiKeyStatus   The type of API key to use
     * @param responseClass  The class to deserialize the response body into
     * @return The result of the operation
     */
    public <T> T GET(String path, ResponseStatus responseStatus, ApiKeyStatus apiKeyStatus, Class<T> responseClass) {
        return readFromResponse(performMvcOperation(get(uri(path)), responseStatus, apiKeyStatus), responseClass);
    }

    /**
     * Perform a POST operation on the TT API at {@code path} with content {@code content}
     *
     * @param path           The path to perform the POST on (e.g. "/employees")
     * @param responseStatus The HTTP status expected from the response
     * @param content        The object that should be serialized into the request body
     * @param apiKeyStatus   The type of API key to use
     * @param responseClass  The class to deserialize the response body into
     * @return The result of the operation
     */
    public <T> T POST(String path, ResponseStatus responseStatus, Object content, ApiKeyStatus apiKeyStatus, Class<T> responseClass) {
        MockHttpServletRequestBuilder request = post(uri(path))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));

        return readFromResponse(performMvcOperation(request, responseStatus, apiKeyStatus), responseClass);
    }

    /**
     * Perform a PATCH operation on the TT API at {@code path} with content {@code content}
     *
     * @param path           The path to perform the PATCH on (e.g. "/employees")
     * @param responseStatus The status to expect from the result
     * @param content        The object that should be serialized into the request body
     * @param apiKeyStatus   The type of API key to use
     * @param responseClass  The
     * @return The result of the operation
     */
    public <T> T PATCH(String path, ResponseStatus responseStatus, Object content, ApiKeyStatus apiKeyStatus, Class<T> responseClass) {
        MockHttpServletRequestBuilder request = patch(uri(path))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));

        return readFromResponse(performMvcOperation(request, responseStatus, apiKeyStatus), responseClass);
    }

    /**
     * Form a URI that is the {@code TT_API_BASE_URL} with {@code path} appended to it
     *
     * @param path The path to append to the base URL
     * @return The formed URI
     */
    private String uri(String path) {
        return TT_API_BASE_URL + path;
    }

    /**
     * Read {@code mvcResult} into an Object of type {@code objectClass}
     *
     * @param mvcResult   The result to read
     * @param objectClass The class to read the result into
     * @return The object read from the response's contents
     */
    public <T> T readFromResponse(MvcResult mvcResult, Class<T> objectClass) {
        try {
            return objectMapper.readerFor(objectClass).readValue(mvcResult.getResponse().getContentAsString());
        } catch (IOException e) {
            throw new RuntimeException("Could not parse mvcResult: " + mvcResult + " into " + objectClass.getSimpleName(), e);
        }
    }

    /**
     * Write an object to JSON
     *
     * @param o Object to write to JSON
     * @return A JSON String representing {@code o}
     */
    public String toJson(Object o) {
        try {
            return objectMapper.writerFor(o.getClass()).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not write object to JSON: " + o, e);
        }
    }

    /**
     * Assert against a failure from the API
     */
    public void assertExceptionResponseIsValid(TacoTuesdayExceptionResponseResource responseResource, HttpStatus status,
                                               boolean retryable, String[] errors)
    {
        assertThat(responseResource.getStatusCode()).isEqualTo(status.value());
        assertThat(responseResource.isRetryable()).isEqualTo(retryable);
        assertThat(responseResource.getOccurredAt()).isBeforeOrEqualTo(Instant.now());
        assertThat(responseResource.getErrors()).containsExactlyInAnyOrder(errors);
    }

    public void assertExceptionResponseIsValid(TacoTuesdayExceptionResponseResource responseResource, HttpStatus status,
                                               boolean retryable, String error)
    {
        assertExceptionResponseIsValid(responseResource, status, retryable, new String[]{error});
    }

    public final String apiKey() {
        return API_KEY;
    }

    public final String invalidApiKey() {
        return INVALID_API_KEY;
    }
}