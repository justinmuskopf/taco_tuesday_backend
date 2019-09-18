package com.respec.tacotuesday.domain;

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

    @OneToMany(mappedBy = "employee")
    private List<IndividualOrder> orders;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
}
