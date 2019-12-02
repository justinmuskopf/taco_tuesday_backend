package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
//@Validated
public class EmployeeResource {
    @Nullable
    @JsonProperty
    private Integer id;

    @JsonProperty
    @NotEmpty(message = "Employee must have a full name!")
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    @NotEmpty(message = "Employee must have a Slack ID!")
    private String slackId;

    @JsonProperty
    private boolean admin = false;
}
