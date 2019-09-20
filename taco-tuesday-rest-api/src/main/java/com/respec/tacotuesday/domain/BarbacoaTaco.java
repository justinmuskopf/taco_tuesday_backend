package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BarbacoaTaco extends Taco {
    @Autowired
    public BarbacoaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.BARBACOA;
        this.price = configuration.getBarbacoaPrice();
    }
}
