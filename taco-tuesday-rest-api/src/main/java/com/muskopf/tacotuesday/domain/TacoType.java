package com.muskopf.tacotuesday.domain;

public enum TacoType {
    BARBACOA("Barbacoa"),
    BEEF_FAJITA("Beef Fajita"),
    CABEZA("Cabeza"),
    CARNITAS("Carnitas"),
    CHICKEN_FAJITA("Chicken Fajita"),
    LENGUA("Lengua"),
    PASTOR("Pastor"),
    TRIPA("Tripa");

    private String prettyName;
    TacoType(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
