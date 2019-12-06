package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.FullName;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator.SlackIdType;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class UpdateEmployeeBatchResource {
    @JsonProperty
    @SlackId(type = SlackIdType.Required)
    private String slackId;

    @JsonProperty
    @FullName
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private boolean admin = false;
}
