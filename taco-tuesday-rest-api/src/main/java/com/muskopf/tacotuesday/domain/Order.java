package com.muskopf.tacotuesday.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@MappedSuperclass
public abstract class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column
    private Float total;

    @Column()
    private Instant createdAt;

    @Column
    private Integer barbacoa = 0;
    @Column
    private Integer beefFajita = 0;
    @Column
    private Integer cabeza = 0;
    @Column
    private Integer carnitas = 0;
    @Column
    private Integer chickenFajita = 0;
    @Column
    private Integer lengua = 0;
    @Column
    private Integer pastor = 0;
    @Column
    private Integer tripa = 0;

    public Order() {
        this.createdAt = Instant.now();
    }

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

    @Override
    public String toString() {
        String sep = System.lineSeparator();

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------").append(sep);
        sb.append("  Barbacoa: ").append(barbacoa).append(sep);
        sb.append("  Beef Fajita: ").append(beefFajita).append(sep);
        sb.append("  Cabeza: ").append(cabeza).append(sep);
        sb.append("  Carnitas: ").append(carnitas).append(sep);
        sb.append("  Chicken Fajita: ").append(chickenFajita).append(sep);
        sb.append("  Lengua: ").append(lengua).append(sep);
        sb.append("  Pastor/Trompo: ").append(pastor).append(sep);
        sb.append("  Tripa: ").append(tripa).append(sep);
        sb.append("  TOTAL: $").append(total).append(sep);
        sb.append("------------------------").append(sep);

        return sb.toString();
    }
}
