package com.muskopf.tacotuesday.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TacoPriceList {
    private BarbacoaTaco barbacoaTaco;
    private BeefFajitaTaco beefFajitaTaco;
    private CabezaTaco cabezaTaco;
    private CarnitasTaco carnitasTaco;
    private ChickenFajitaTaco chickenFajitaTaco;
    private LenguaTaco lenguaTaco;
    private PastorTaco pastorTaco;
    private TripaTaco tripaTaco;

    @Autowired
    public TacoPriceList(BarbacoaTaco barbacoaTaco,
                         BeefFajitaTaco beefFajitaTaco,
                         CabezaTaco cabezaTaco,
                         CarnitasTaco carnitasTaco,
                         ChickenFajitaTaco chickenFajitaTaco,
                         LenguaTaco lenguaTaco,
                         PastorTaco pastorTaco,
                         TripaTaco tripaTaco)
    {
        this.barbacoaTaco = barbacoaTaco;
        this.beefFajitaTaco = beefFajitaTaco;
        this.cabezaTaco = cabezaTaco;
        this.carnitasTaco = carnitasTaco;
        this.chickenFajitaTaco = chickenFajitaTaco;
        this.lenguaTaco = lenguaTaco;
        this.pastorTaco = pastorTaco;
        this.tripaTaco = tripaTaco;
    }

    public BarbacoaTaco getBarbacoaTaco() {
        return barbacoaTaco;
    }

    public void setBarbacoaTaco(BarbacoaTaco barbacoaTaco) {
        this.barbacoaTaco = barbacoaTaco;
    }

    public BeefFajitaTaco getBeefFajitaTaco() {
        return beefFajitaTaco;
    }

    public void setBeefFajitaTaco(BeefFajitaTaco beefFajitaTaco) {
        this.beefFajitaTaco = beefFajitaTaco;
    }

    public CabezaTaco getCabezaTaco() {
        return cabezaTaco;
    }

    public void setCabezaTaco(CabezaTaco cabezaTaco) {
        this.cabezaTaco = cabezaTaco;
    }

    public CarnitasTaco getCarnitasTaco() {
        return carnitasTaco;
    }

    public void setCarnitasTaco(CarnitasTaco carnitasTaco) {
        this.carnitasTaco = carnitasTaco;
    }

    public ChickenFajitaTaco getChickenFajitaTaco() {
        return chickenFajitaTaco;
    }

    public void setChickenFajitaTaco(ChickenFajitaTaco chickenFajitaTaco) {
        this.chickenFajitaTaco = chickenFajitaTaco;
    }

    public LenguaTaco getLenguaTaco() {
        return lenguaTaco;
    }

    public void setLenguaTaco(LenguaTaco lenguaTaco) {
        this.lenguaTaco = lenguaTaco;
    }

    public PastorTaco getPastorTaco() {
        return pastorTaco;
    }

    public void setPastorTaco(PastorTaco pastorTaco) {
        this.pastorTaco = pastorTaco;
    }

    public TripaTaco getTripaTaco() {
        return tripaTaco;
    }

    public void setTripaTaco(TripaTaco tripaTaco) {
        this.tripaTaco = tripaTaco;
    }
}
