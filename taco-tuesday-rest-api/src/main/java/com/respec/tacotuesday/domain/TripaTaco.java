package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TripaTaco extends Taco {
    @Autowired
    public TripaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.TRIPA;
        this.price = configuration.getTripaPrice();
    }
}
