package com.muskopf.tacotuesday.domain;

import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.muskopf.tacotuesday.config.TacoTuesdayApiProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class TacoPriceList {
    private List<Taco> tacos = new ArrayList<>();

    @Autowired
    public TacoPriceList(TacoTuesdayApiProperties properties) {
        tacos.add(new Taco(TacoType.barbacoa, properties.getBarbacoaPrice()));
        tacos.add(new Taco(TacoType.beefFajita, properties.getBeefFajitaPrice()));
        tacos.add(new Taco(TacoType.cabeza, properties.getCabezaPrice()));
        tacos.add(new Taco(TacoType.carnitas, properties.getCarnitasPrice()));
        tacos.add(new Taco(TacoType.chickenFajita, properties.getChickenFajitaPrice()));
        tacos.add(new Taco(TacoType.lengua, properties.getLenguaPrice()));
        tacos.add(new Taco(TacoType.pastor, properties.getPastorPrice()));
        tacos.add(new Taco(TacoType.tripa, properties.getTripaPrice()));
    }

    public List<Taco> getPriceList() {
        return tacos;
    }
}
