package com.muskopf.tacotuesday.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public abstract class Order extends DomainObject {
    @Column
    private Float total;

    @Column(name = "barbacoa")
    private Integer barbacoa = 0;
    @Column(name = "beef_fajita")
    private Integer beefFajita = 0;
    @Column(name = "cabeza")
    private Integer cabeza = 0;
    @Column(name = "carnitas")
    private Integer carnitas = 0;
    @Column(name = "chicken_fajita")
    private Integer chickenFajita = 0;
    @Column(name = "lengua")
    private Integer lengua = 0;
    @Column(name = "pastor")
    private Integer pastor = 0;
    @Column(name = "tripa")
    private Integer tripa = 0;

    public void merge(Order newOrder) {
        this.barbacoa = newOrder.barbacoa != null ? newOrder.barbacoa : barbacoa;
        this.beefFajita = newOrder.beefFajita != null ? newOrder.beefFajita : beefFajita;
        this.cabeza = newOrder.cabeza != null ? newOrder.cabeza : cabeza;
        this.carnitas = newOrder.carnitas != null ? newOrder.carnitas : carnitas;
        this.chickenFajita = newOrder.chickenFajita != null ? newOrder.chickenFajita : chickenFajita;
        this.lengua = newOrder.lengua != null ? newOrder.lengua : lengua;
        this.pastor = newOrder.pastor != null ? newOrder.pastor : pastor;
        this.tripa = newOrder.tripa != null ? newOrder.tripa : tripa;
    }

    public void setTacoCount(TacoType tacoType, int count) {
        if (count < 0) {
            throw new RuntimeException("Invalid " + tacoType.prettyName() + " Count: " + count + "!");
        }

        switch (tacoType) {
            case chickenFajita:
                this.chickenFajita = count;
                break;
            case beefFajita:
                this.beefFajita = count;
                break;
            case carnitas:
                this.carnitas = count;
                break;
            case cabeza:
                this.cabeza = count;
                break;
            case barbacoa:
                this.barbacoa = count;
                break;
            case pastor:
                this.pastor = count;
                break;
            case lengua:
                this.lengua = count;
                break;
            case tripa:
                this.tripa = count;
                break;
            default:
                throw new RuntimeException("Invalid Taco Type: " + tacoType.prettyName() + "!");
        }
    }

    public Integer getTacoCount(TacoType tacoType) {
        switch (tacoType) {
            case chickenFajita:
                return chickenFajita;
            case beefFajita:
                return beefFajita;
            case carnitas:
                return carnitas;
            case cabeza:
                return cabeza;
            case barbacoa:
                return barbacoa;
            case pastor:
                return pastor;
            case lengua:
                return lengua;
            case tripa:
                return tripa;
            default:
                throw new RuntimeException("Invalid Taco Type: " + tacoType.prettyName() + "!");
        }
    }

    @Override
    public String toString() {
        String sep = System.lineSeparator();

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------").append(sep);
        sb.append("  ID: ").append(id).append(sep);
        sb.append("------------------------").append(sep);
        sb.append("  Barbacoa: ").append(barbacoa).append(sep);
        sb.append("  Beef Fajita: ").append(beefFajita).append(sep);
        sb.append("  Cabeza: ").append(cabeza).append(sep);
        sb.append("  Carnitas: ").append(carnitas).append(sep);
        sb.append("  Chicken Fajita: ").append(chickenFajita).append(sep);
        sb.append("  Lengua: ").append(lengua).append(sep);
        sb.append("  Pastor/Trompo: ").append(pastor).append(sep);
        sb.append("  Tripa: ").append(tripa).append(sep).append(sep);
        sb.append("  TOTAL: $").append(total).append(sep);
        sb.append("------------------------").append(sep);

        return sb.toString();
    }
}
