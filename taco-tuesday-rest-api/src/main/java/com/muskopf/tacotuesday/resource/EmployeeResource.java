package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class EmployeeResource {
    @Nullable
    @JsonProperty
    private Integer id;

    @JsonProperty
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private String slackId;

    @JsonProperty
    private boolean admin;
}
