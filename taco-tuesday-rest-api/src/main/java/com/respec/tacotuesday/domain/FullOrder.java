package com.respec.tacotuesday.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class FullOrder extends Order {
    @Column(name = "fk_full_order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer fullOrderId;

    @OneToMany(mappedBy = "fullOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<IndividualOrder> individualOrders;

    public Integer getFullOrderId() { return fullOrderId; }
    public void setFullOrderId(Integer fullOrderId) { this.fullOrderId = fullOrderId; }

    public List<IndividualOrder> getIndividualOrders() { return individualOrders; }
    public void setIndividualOrders(List<IndividualOrder> individualOrders) { this.individualOrders = individualOrders; }
    public FullOrder individualOrders(List<IndividualOrder> individualOrders) {
        this.individualOrders = individualOrders;
        return this;
    }
}
