package com.respec.tacotuesday.domain;

import com.respec.tacotuesday.config.TacoTuesdayConfiguration;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChickenFajitaTaco extends Taco {
    @Autowired
    public ChickenFajitaTaco(TacoTuesdayConfiguration configuration) {
        this.type = TacoType.CHICKEN_FAJITA;
        this.price = configuration.getChickenFajitaPrice();
    }
}
