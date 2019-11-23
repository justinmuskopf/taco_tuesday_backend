package com.muskopf.tacotuesday.domain;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TacoPriceList {
    private List<Taco> tacos = new ArrayList<>();

    @Autowired
    public TacoPriceList(TacoTuesdayApiConfiguration config)
    {
        tacos.add(new Taco(TacoType.barbacoa, config.getBarbacoaPrice()));
        tacos.add(new Taco(TacoType.beefFajita, config.getBeefFajitaPrice()));
        tacos.add(new Taco(TacoType.cabeza, config.getCabezaPrice()));
        tacos.add(new Taco(TacoType.carnitas, config.getCarnitasPrice()));
        tacos.add(new Taco(TacoType.chickenFajita, config.getChickenFajitaPrice()));
        tacos.add(new Taco(TacoType.lengua, config.getLenguaPrice()));
        tacos.add(new Taco(TacoType.pastor, config.getPastorPrice()));
        tacos.add(new Taco(TacoType.tripa, config.getTripaPrice()));
    }

    public List<Taco> getPriceList() {
        return tacos;
    }
}
