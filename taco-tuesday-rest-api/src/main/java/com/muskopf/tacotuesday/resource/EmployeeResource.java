package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.FullName;
import com.muskopf.tacotuesday.api.validator.SlackId;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

import static com.muskopf.tacotuesday.api.validator.SlackIdValidator.SlackIdType.Required;

@Data
@Validated
public class EmployeeResource {
    @Nullable
    @JsonProperty
    private Integer id;

    @JsonProperty
    @FullName
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    @SlackId(type = Required)
    private String slackId;

    @JsonProperty
    private boolean admin = false;
}
