package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeResource {
    @JsonProperty
    private int id;

    @JsonProperty
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private String slackId;

    @JsonProperty
    private boolean admin;
}
