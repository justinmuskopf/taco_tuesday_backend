package com.muskopf.tacotuesday.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "taco-tuesday")
public class TacoTuesdayApiProperties {

    private Float barbacoaPrice;

    private Float beefFajitaPrice;

    private Float cabezaPrice;

    private Float carnitasPrice;

    private Float chickenFajitaPrice;

    private Float lenguaPrice;

    private Float pastorPrice;

    private Float tripaPrice;

    private boolean emailEnabled = true;
}
