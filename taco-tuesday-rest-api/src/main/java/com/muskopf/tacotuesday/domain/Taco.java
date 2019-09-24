package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.*;

public class Taco {
    private Float price;
    private TacoType type;

    public Taco(TacoType type, Float price) {
        this.type = type;
        this.price = price;
    }

    public Float getPrice() {return price;}
    public void setPrice(Float price) {this.price = price;}

    public TacoType getType() {return type;}
    public void setType(TacoType type) {this.type = type;}
}
