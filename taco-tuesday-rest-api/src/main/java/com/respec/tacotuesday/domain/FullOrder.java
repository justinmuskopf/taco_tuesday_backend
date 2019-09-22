package com.respec.tacotuesday.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class FullOrder extends Order {
    @Column(name = "fk_full_order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer fullOrderId;

    @OneToMany(mappedBy = "fullOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<IndividualOrder> individualOrders;

    public Integer getFullOrderId() { return fullOrderId; }
    public void setFullOrderId(Integer fullOrderId) { this.fullOrderId = fullOrderId; }

    public Set<IndividualOrder> getIndividualOrders() { return individualOrders; }
    public void setIndividualOrders(Set<IndividualOrder> individualOrders) { this.individualOrders = individualOrders; }
    public FullOrder individualOrders(Set<IndividualOrder> individualOrders) {
        this.individualOrders = individualOrders;
        return this;
    }
}
