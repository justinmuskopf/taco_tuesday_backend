package com.respec.tacotuesday.domain;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "tacoType")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = BarbacoaTaco.class, name = "BARBACOA"),
        @JsonSubTypes.Type(value = BeefFajitaTaco.class, name = "BEEF_FAJITA"),
        @JsonSubTypes.Type(value = CabezaTaco.class, name = "CABEZA"),
        @JsonSubTypes.Type(value = CarnitasTaco.class, name = "CARNITAS"),
        @JsonSubTypes.Type(value = ChickenFajitaTaco.class, name = "CHICKEN_FAJITA"),
        @JsonSubTypes.Type(value = LenguaTaco.class, name = "LENGUA"),
        @JsonSubTypes.Type(value = PastorTaco.class, name = "PASTOR"),
        @JsonSubTypes.Type(value = TripaTaco.class, name = "TRIPA"),
})
public abstract class Taco {
    protected TacoType type;
    protected Float price;

    public TacoType getType() {
        return type;
    }

    public void setType(TacoType tacoType) {
        this.type = tacoType;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
