package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true, exclude = "individualOrders")
public class FullOrder extends Order {
    @OneToMany(mappedBy = "fullOrder", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Set<IndividualOrder> individualOrders;

    @Override
    public String toString() {
        String sep = System.lineSeparator();

        StringBuilder sb = new StringBuilder();
        sb.append("FULL ORDER #").append(id).append(":").append(sep);
        sb.append(super.toString());
        sb.append(sep).append(sep);

        sb.append("INDIVIDUAL ORDERS:").append(sep);

        int orderNum = 1;
        for (IndividualOrder order : individualOrders) {
            sb.append("#").append(orderNum++).append(". ").append(order.toString());
        }

        return sb.toString();
    }
}
