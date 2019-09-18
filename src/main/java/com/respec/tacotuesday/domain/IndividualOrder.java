package com.respec.tacotuesday.domain;

import javax.persistence.*;

@Entity
public class IndividualOrder extends Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order_id", referencedColumnName = "id")
    private FullOrder fullOrder;

    @ManyToOne
    @JoinColumn(name = "fk_employee_id", referencedColumnName = "id")
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
