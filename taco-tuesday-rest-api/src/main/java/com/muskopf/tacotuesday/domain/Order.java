package com.muskopf.tacotuesday.domain;

import javax.persistence.*;
//import java.math.Float;
import java.time.Instant;

//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Order id(Integer id) {
        this.id = id;
        return this;
    }

    public Float getTotal() {
        return total;
    }
    public void setTotal(Float total) {
        this.total = total;
    }
    public Order total(Float total) {
        this.total = total;
        return this;
    }

    public Integer getBarbacoa() {
        return barbacoa;
    }
    public void setBarbacoa(Integer barbacoa) {
        this.barbacoa = barbacoa;
    }
    public Order barbacoa(Integer barbacoa) {
        this.barbacoa = barbacoa;
        return this;
    }

    public Integer getBeefFajita() {
        return beefFajita;
    }
    public void setBeefFajita(Integer beefFajita) {
        this.beefFajita = beefFajita;
    }
    public Order beefFajita(Integer beefFajita) {
        this.beefFajita = beefFajita;
        return this;
    }

    public Integer getCabeza() {
        return cabeza;
    }
    public void setCabeza(Integer cabeza) {
        this.cabeza = cabeza;
    }
    public Order cabeza(Integer cabeza) {
        this.cabeza = cabeza;
        return this;
    }

    public Integer getCarnitas() {
        return carnitas;
    }
    public void setCarnitas(Integer carnitas) {
        this.carnitas = carnitas;
    }
    public Order carnitas(Integer carnitas) {
        this.carnitas = carnitas;
        return this;
    }

    public Integer getChickenFajita() {
        return chickenFajita;
    }
    public void setChickenFajita(Integer chickenFajita) {
        this.chickenFajita = chickenFajita;
    }
    public Order chickenFajita(Integer chickenFajita) {
        this.chickenFajita = chickenFajita;
        return this;
    }

    public Integer getLengua() {
        return lengua;
    }
    public void setLengua(Integer lengua) {
        this.lengua = lengua;
    }
    public Order lengua(Integer lengua) {
        this.lengua = lengua;
        return this;
    }

    public Integer getPastor() {
        return pastor;
    }
    public void setPastor(Integer pastor) {
        this.pastor = pastor;
    }
    public Order pastor(Integer pastor) {
        this.pastor = pastor;
        return this;
    }

    public Integer getTripa() {
        return tripa;
    }
    public void setTripa(Integer tripa) {
        this.tripa = tripa;
    }
    public Order tripa(Integer tripa) {
        this.tripa = tripa;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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
