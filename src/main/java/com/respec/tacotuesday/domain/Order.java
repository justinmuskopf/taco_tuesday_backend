package com.respec.tacotuesday.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;

    @Column
    private BigDecimal total;

    @Column
    private LocalDate date;

    @Column
    private Integer barbacoa;
    @Column
    private Integer beefFajita;
    @Column
    private Integer cabeza;
    @Column
    private Integer carnitas;
    @Column
    private Integer chickenFajita;
    @Column
    private Integer lengua;
    @Column
    private Integer pastor;
    @Column
    private Integer tripa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Integer getBarbacoa() {
        return barbacoa;
    }

    public void setBarbacoa(Integer barbacoa) {
        this.barbacoa = barbacoa;
    }

    public Integer getBeefFajita() {
        return beefFajita;
    }

    public void setBeefFajita(Integer beefFajita) {
        this.beefFajita = beefFajita;
    }

    public Integer getCabeza() {
        return cabeza;
    }

    public void setCabeza(Integer cabeza) {
        this.cabeza = cabeza;
    }

    public Integer getCarnitas() {
        return carnitas;
    }

    public void setCarnitas(Integer carnitas) {
        this.carnitas = carnitas;
    }

    public Integer getChickenFajita() {
        return chickenFajita;
    }

    public void setChickenFajita(Integer chickenFajita) {
        this.chickenFajita = chickenFajita;
    }

    public Integer getLengua() {
        return lengua;
    }

    public void setLengua(Integer lengua) {
        this.lengua = lengua;
    }

    public Integer getPastor() {
        return pastor;
    }

    public void setPastor(Integer pastor) {
        this.pastor = pastor;
    }

    public Integer getTripa() {
        return tripa;
    }

    public void setTripa(Integer tripa) {
        this.tripa = tripa;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void addTaco(TacoType tacoType) {
        switch (tacoType) {
            case BARBACOA:
            case BEEF_FAJITA:
            case CABEZA:
            case CARNITAS:
            case CHICKEN_FAJITA:
            case LENGUA:
            case PASTOR:
            case TRIPA:
        }
    }
}
