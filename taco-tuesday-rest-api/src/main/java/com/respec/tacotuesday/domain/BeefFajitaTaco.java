package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeefFajitaTaco extends Taco {
    @Autowired
    public BeefFajitaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.BEEF_FAJITA;
        this.price = configuration.getBeefFajitaPrice();
    }
}
