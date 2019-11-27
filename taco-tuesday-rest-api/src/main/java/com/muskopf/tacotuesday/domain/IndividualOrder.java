package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class IndividualOrder extends Order {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_full_order_id", referencedColumnName = "id")
    @JsonBackReference
    private FullOrder fullOrder;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_employee_id", referencedColumnName = "id")
    private Employee employee;

    @Override
    public String toString() {
        String sep = System.lineSeparator();

        StringBuilder sb = new StringBuilder();
        sb.append("EMPLOYEE ORDER: ").append(employee).append(sep);
        sb.append(super.toString());

        return sb.toString();
    }
}
