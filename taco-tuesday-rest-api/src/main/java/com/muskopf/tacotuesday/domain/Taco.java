package com.muskopf.tacotuesday.domain;

public class Taco {
    private Float price;
    private TacoType type;
    private String name;

    public Taco(TacoType type, Float price) {
        this.type = type;
        this.price = price;
        this.name = type.getPrettyName();
    }

    public Float getPrice() {return price;}
    public void setPrice(Float price) {this.price = price;}

    public TacoType getType() {return type;}
    public void setType(TacoType type) {this.type = type;}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
