package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.Instant;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "orderType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IndividualOrderResource.class, name = "individualOrder"),
        @JsonSubTypes.Type(value = FullOrderResource.class, name = "fullOrder")
})
public class OrderResource {
    @JsonProperty
    protected Integer id;

    @JsonProperty
    private Float total;

    @JsonProperty
    private Instant createdAt;

    @JsonProperty
    private Integer barbacoa = 0;

    @JsonProperty
    private Integer beefFajita = 0;

    @JsonProperty
    private Integer cabeza = 0;

    @JsonProperty
    private Integer carnitas = 0;

    @JsonProperty
    private Integer chickenFajita = 0;

    @JsonProperty
    private Integer lengua = 0;

    @JsonProperty
    private Integer pastor = 0;

    @JsonProperty
    private Integer tripa = 0;
}
