package com.muskopf.tacotuesday.domain;

public enum TacoType {
    barbacoa("Barbacoa"),
    beefFajita("Beef Fajita"),
    cabeza("Cabeza"),
    carnitas("Carnitas"),
    chickenFajita("Chicken Fajita"),
    lengua("Lengua"),
    pastor("Pastor"),
    tripa("Tripa");

    private String prettyName;

    TacoType(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
