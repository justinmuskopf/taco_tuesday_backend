package com.muskopf.tacotuesday.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "taco-tuesday")
public class TacoTuesdayApiProperties {

    @NotNull
    private Float barbacoaPrice;

    @NotNull
    private Float beefFajitaPrice;

    @NotNull
    private Float cabezaPrice;

    @NotNull
    private Float carnitasPrice;

    @NotNull
    private Float chickenFajitaPrice;

    @NotNull
    private Float lenguaPrice;

    @NotNull
    private Float pastorPrice;

    @NotNull
    private Float tripaPrice;

    private boolean emailEnabled = true;
}
