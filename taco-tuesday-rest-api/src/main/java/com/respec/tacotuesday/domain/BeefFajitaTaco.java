package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeefFajitaTaco extends Taco {
    @Autowired
    public BeefFajitaTaco(TacoTuesdayApiConfiguration configuration) {
        this.price = configuration.getBeefFajitaPrice();
    }
}
