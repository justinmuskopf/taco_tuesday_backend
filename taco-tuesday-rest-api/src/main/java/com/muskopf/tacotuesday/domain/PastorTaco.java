package com.muskopf.tacotuesday.domain;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PastorTaco extends Taco {
    @Autowired
    public PastorTaco(TacoTuesdayApiConfiguration configuration) {
        this.price = configuration.getPastorPrice();
    }
}
