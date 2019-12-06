package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.FullName;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator.SlackIdType;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
public class NewEmployeeResource {
    @JsonProperty
    @FullName
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    @SlackId(type = SlackIdType.New)
    private String slackId;

    @JsonProperty
    private boolean admin = false;
}
