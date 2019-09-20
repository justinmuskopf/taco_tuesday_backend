package com.respec.tacotuesday.domain;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String nickName;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.PERSIST)
    private List<IndividualOrder> orders;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public Employee firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public Employee lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public Employee nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public List<IndividualOrder> getOrders() {
        return orders;
    }
    public void setOrders(List<IndividualOrder> orders) {
        this.orders = orders;
    }
    public Employee orders(List<IndividualOrder> orders) {
        this.orders = orders;
        return this;
    }
}
