package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.muskopf.tacotuesday.api.validator.FullName;
import com.muskopf.tacotuesday.api.validator.FullNameValidator;
import com.muskopf.tacotuesday.api.validator.FullNameValidator.FullNameType;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator;
import lombok.Data;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static com.muskopf.tacotuesday.api.validator.FullNameValidator.FullNameType.Optional;

@Data
@Validated
public class UpdateEmployeeResource {
    @JsonProperty
    @FullName(type = Optional)
    private String fullName;

    @JsonProperty
    private String nickName;

    @JsonProperty
    private boolean admin = false;
}
