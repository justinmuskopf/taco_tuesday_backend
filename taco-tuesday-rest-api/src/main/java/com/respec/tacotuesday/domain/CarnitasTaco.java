package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarnitasTaco extends Taco {
    @Autowired
    public CarnitasTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.CARNITAS;
        this.price = configuration.getCarnitasPrice();
    }
}
