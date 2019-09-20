package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PastorTaco extends Taco {
    @Autowired
    public PastorTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.PASTOR;
        this.price = configuration.getPastorPrice();
    }
}
