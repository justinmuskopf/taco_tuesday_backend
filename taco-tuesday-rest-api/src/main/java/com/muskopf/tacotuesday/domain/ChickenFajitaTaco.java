package com.muskopf.tacotuesday.domain;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChickenFajitaTaco extends Taco {
    @Autowired
    public ChickenFajitaTaco(TacoTuesdayApiConfiguration configuration) {
        this.price = configuration.getChickenFajitaPrice();
    }
}
