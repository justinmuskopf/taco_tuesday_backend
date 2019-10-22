package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class IndividualOrder extends Order {
    @ManyToOne(fetch = FetchType.LAZY)//fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_full_order_id", referencedColumnName = "id")
    @JsonBackReference
    private FullOrder fullOrder;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_employee_id", referencedColumnName = "id")
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    public IndividualOrder employee(Employee employee) {
        setEmployee(employee);
        return this;
    }

    public FullOrder getFullOrder() { return fullOrder; }
    public void setFullOrder(FullOrder fullOrder) { this.fullOrder = fullOrder; }
    public IndividualOrder fullOrder(FullOrder fullOrder) {
        this.fullOrder = fullOrder;
        return this;
    }
}
