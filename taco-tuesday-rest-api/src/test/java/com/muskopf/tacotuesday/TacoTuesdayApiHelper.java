package com.muskopf.tacotuesday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Component
@ActiveProfiles("test")
public class TacoTuesdayApiHelper {
    private static final String TT_API_BASE_URL = "/taco-tuesday/v1";
    private static final String API_KEY = UUID.randomUUID().toString();

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

    /**
     * Convenience ENUM to represent HTTP statuses
     */
    public enum ResponseStatus {
        OK(status().isOk()),
        CREATED(status().isCreated());

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
     * @param useApiKey      Whether or not the request should use an API key
     * @return The result of the operation
     */
    private MvcResult performMvcOperation(MockHttpServletRequestBuilder request, ResponseStatus responseStatus, boolean useApiKey)
    {
        if (useApiKey) {
            request.param("apiKey", API_KEY);
        }

        // Perform request
        try {
            return mockMvc.perform(request)
                    .andExpect(responseStatus.status)
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
        } catch (Exception e) {
            throw new RuntimeException("Error performing MockMvc operation: " + request, e);
        }
    }

    /**
     * Perform a GET operation on the TT API at {@code path}
     *
     * @param path          The path to perform the GET on (e.g. "/employees")
     * @param responseClass The status to expect from the result
     * @return The result of the operation
     */
    public <T> T GET(String path, ResponseStatus responseStatus, boolean useApiKey, Class<T> responseClass) {
        return readFromResponse(performMvcOperation(get(uri(path)), responseStatus, useApiKey), responseClass);
    }

    /**
     * Perform a POST operation on the TT API at {@code path} with content {@code content}
     *
     * @param path          The path to perform the POST on (e.g. "/employees")
     * @param responseClass The status to expect from the result
     * @param content       The object that should be serialized into the request body
     * @return The result of the operation
     */
    public <T> T POST(String path, ResponseStatus responseStatus, Object content, Class<T> responseClass) {
        MockHttpServletRequestBuilder request = post(uri(path))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));

        return readFromResponse(performMvcOperation(request, responseStatus, true), responseClass);
    }

    /**
     * Perform a PATCH operation on the TT API at {@code path} with content {@code content}
     *
     * @param path           The path to perform the PATCH on (e.g. "/employees")
     * @param responseStatus The status to expect from the result
     * @param content        The object that should be serialized into the request body
     * @return The result of the operation
     */
    public <T> T PATCH(String path, ResponseStatus responseStatus, Object content, Class<T> responseClass) {
        MockHttpServletRequestBuilder request = patch(uri(path))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));

        return readFromResponse(performMvcOperation(request, responseStatus, true), responseClass);
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
}
