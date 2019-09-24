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
        tacos.add(new Taco(TacoType.BARBACOA, config.getBarbacoaPrice()));
        tacos.add(new Taco(TacoType.BEEF_FAJITA, config.getBeefFajitaPrice()));
        tacos.add(new Taco(TacoType.CABEZA, config.getCabezaPrice()));
        tacos.add(new Taco(TacoType.CARNITAS, config.getCarnitasPrice()));
        tacos.add(new Taco(TacoType.CHICKEN_FAJITA, config.getChickenFajitaPrice()));
        tacos.add(new Taco(TacoType.LENGUA, config.getLenguaPrice()));
        tacos.add(new Taco(TacoType.PASTOR, config.getPastorPrice()));
        tacos.add(new Taco(TacoType.TRIPA, config.getTripaPrice()));
    }

    public List<Taco> getPriceList() {
        return tacos;
    }
}
