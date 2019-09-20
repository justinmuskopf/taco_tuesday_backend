package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CabezaTaco extends Taco {
    @Autowired
    public CabezaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.CABEZA;
        this.price = configuration.getCabezaPrice();
    }
}
