package com.muskopf.tacotuesday.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

//import java.math.Float;


@ConfigurationProperties(prefix = "taco-tuesday")
public class TacoTuesdayApiConfiguration {
    //@NotNull
    private Float barbacoaPrice;
    //@NotNull
    private Float beefFajitaPrice;
    //@NotNull
    private Float cabezaPrice;
    //@NotNull
    private Float carnitasPrice;
    //@NotNull
    private Float chickenFajitaPrice;
    //@NotNull
    private Float lenguaPrice;
    //@NotNull
    private Float pastorPrice;
    //@NotNull
    private Float tripaPrice;

    public Float getBarbacoaPrice() {
        return barbacoaPrice;
    }

    public void setBarbacoaPrice(Float barbacoaPrice) {
        this.barbacoaPrice = barbacoaPrice;
    }

    public Float getBeefFajitaPrice() {
        return beefFajitaPrice;
    }

    public void setBeefFajitaPrice(Float beefFajitaPrice) {
        this.beefFajitaPrice = beefFajitaPrice;
    }

    public Float getCabezaPrice() {
        return cabezaPrice;
    }

    public void setCabezaPrice(Float cabezaPrice) {
        this.cabezaPrice = cabezaPrice;
    }

    public Float getCarnitasPrice() {
        return carnitasPrice;
    }

    public void setCarnitasPrice(Float carnitasPrice) {
        this.carnitasPrice = carnitasPrice;
    }

    public Float getChickenFajitaPrice() {
        return chickenFajitaPrice;
    }

    public void setChickenFajitaPrice(Float chickenFajitaPrice) {
        this.chickenFajitaPrice = chickenFajitaPrice;
    }

    public Float getLenguaPrice() {
        return lenguaPrice;
    }

    public void setLenguaPrice(Float lenguaPrice) {
        this.lenguaPrice = lenguaPrice;
    }

    public Float getPastorPrice() {
        return pastorPrice;
    }

    public void setPastorPrice(Float pastorPrice) {
        this.pastorPrice = pastorPrice;
    }

    public Float getTripaPrice() {
        return tripaPrice;
    }

    public void setTripaPrice(Float tripaPrice) {
        this.tripaPrice = tripaPrice;
    }
}
