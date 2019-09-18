package com.respec.tacotuesday.config;

import com.respec.tacotuesday.domain.TacoType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "taco-tuesday")
@Validated
public class TacoTuesdayConfiguration {
    @NotNull
    private BigDecimal barbacoaPrice;
    @NotNull
    private BigDecimal beefFajitaPrice;
    @NotNull
    private BigDecimal cabezaPrice;
    @NotNull
    private BigDecimal carnitasPrice;
    @NotNull
    private BigDecimal chickenFajitaPrice;
    @NotNull
    private BigDecimal lenguaPrice;
    @NotNull
    private BigDecimal pastorPrice;
    @NotNull
    private BigDecimal tripaPrice;

    public BigDecimal getBarbacoaPrice() {
        return barbacoaPrice;
    }

    public void setBarbacoaPrice(BigDecimal barbacoaPrice) {
        this.barbacoaPrice = barbacoaPrice;
    }

    public BigDecimal getBeefFajitaPrice() {
        return beefFajitaPrice;
    }

    public void setBeefFajitaPrice(BigDecimal beefFajitaPrice) {
        this.beefFajitaPrice = beefFajitaPrice;
    }

    public BigDecimal getCabezaPrice() {
        return cabezaPrice;
    }

    public void setCabezaPrice(BigDecimal cabezaPrice) {
        this.cabezaPrice = cabezaPrice;
    }

    public BigDecimal getCarnitasPrice() {
        return carnitasPrice;
    }

    public void setCarnitasPrice(BigDecimal carnitasPrice) {
        this.carnitasPrice = carnitasPrice;
    }

    public BigDecimal getChickenFajitaPrice() {
        return chickenFajitaPrice;
    }

    public void setChickenFajitaPrice(BigDecimal chickenFajitaPrice) {
        this.chickenFajitaPrice = chickenFajitaPrice;
    }

    public BigDecimal getLenguaPrice() {
        return lenguaPrice;
    }

    public void setLenguaPrice(BigDecimal lenguaPrice) {
        this.lenguaPrice = lenguaPrice;
    }

    public BigDecimal getPastorPrice() {
        return pastorPrice;
    }

    public void setPastorPrice(BigDecimal pastorPrice) {
        this.pastorPrice = pastorPrice;
    }

    public BigDecimal getTripaPrice() {
        return tripaPrice;
    }

    public void setTripaPrice(BigDecimal tripaPrice) {
        this.tripaPrice = tripaPrice;
    }

    public final Map<TacoType, BigDecimal> getTacoPriceMap() {
        Map<TacoType, BigDecimal> tacoPriceMap = new HashMap<>();
        tacoPriceMap.put(TacoType.BARBACOA, barbacoaPrice);
        tacoPriceMap.put(TacoType.BEEF_FAJITA, beefFajitaPrice);
        tacoPriceMap.put(TacoType.CABEZA, cabezaPrice);
        tacoPriceMap.put(TacoType.CARNITAS, carnitasPrice);
        tacoPriceMap.put(TacoType.CHICKEN_FAJITA, chickenFajitaPrice);
        tacoPriceMap.put(TacoType.LENGUA, lenguaPrice);
        tacoPriceMap.put(TacoType.PASTOR, pastorPrice);
        tacoPriceMap.put(TacoType.TRIPA, tripaPrice);

        return tacoPriceMap;
    }
}
