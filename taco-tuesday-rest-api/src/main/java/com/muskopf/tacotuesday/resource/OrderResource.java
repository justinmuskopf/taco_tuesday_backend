package com.muskopf.tacotuesday.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.muskopf.tacotuesday.api.validator.Price;
import com.muskopf.tacotuesday.api.validator.TacoCount;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
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
@Validated
public class OrderResource {
    @JsonProperty
    protected Integer id;

    @JsonProperty
    @Price
    private Float total;

    @JsonProperty
    private Instant createdAt;

    @JsonProperty
    @TacoCount
    private Integer barbacoa = 0;

    @JsonProperty
    @TacoCount
    private Integer beefFajita = 0;

    @JsonProperty
    @TacoCount
    private Integer cabeza = 0;

    @JsonProperty
    @TacoCount
    private Integer carnitas = 0;

    @JsonProperty
    @TacoCount
    private Integer chickenFajita = 0;

    @JsonProperty
    @TacoCount
    private Integer lengua = 0;

    @JsonProperty
    @TacoCount
    private Integer pastor = 0;

    @JsonProperty
    @TacoCount
    private Integer tripa = 0;
}
