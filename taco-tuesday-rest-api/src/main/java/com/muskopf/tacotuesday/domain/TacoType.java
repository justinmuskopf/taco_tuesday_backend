package com.muskopf.tacotuesday.domain;

public enum TacoType {
    barbacoa("Barbacoa", "barbacoa"),
    beefFajita("Beef Fajita", "beef_fajita"),
    cabeza("Cabeza", "cabeza"),
    carnitas("Carnitas", "carnitas"),
    chickenFajita("Chicken Fajita", "chicken_fajita"),
    lengua("Lengua", "lengua"),
    pastor("Pastor", "pastor"),
    tripa("Tripa", "tripa");

    private String prettyName;
    private String columnName;

    TacoType(String prettyName, String columnName) {
        this.prettyName = prettyName;
        this.columnName = columnName;
    }

    public String prettyName() { return prettyName; }
    public String columnName() { return columnName; }
}
