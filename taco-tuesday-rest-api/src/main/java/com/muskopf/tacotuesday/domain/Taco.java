package com.muskopf.tacotuesday.domain;

import lombok.Data;

@Data
public class Taco {
    private Float price;
    private TacoType type;
    private String name;

    // Appease the gods
    public Taco() {
    }

    public Taco(TacoType type, Float price) {
        this.type = type;
        this.price = price;
        this.name = type.prettyName();
    }
}
