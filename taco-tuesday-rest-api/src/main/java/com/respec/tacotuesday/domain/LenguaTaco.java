package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LenguaTaco extends Taco {
    @Autowired
    public LenguaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.LENGUA;
        this.price = configuration.getLenguaPrice();
    }
}
