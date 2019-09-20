package com.respec.tacotuesday.domain;

import javax.persistence.*;

@Entity
public class IndividualOrder extends Order {
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_full_order_id", referencedColumnName = "id")
    private FullOrder fullOrder;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_employee_id", referencedColumnName = "id")
    private Employee employee;

    private Integer employeeId;

    public Employee getEmployee() {
        return employee;
    }
    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee.getId();
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

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }
}
