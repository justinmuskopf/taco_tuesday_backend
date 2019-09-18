package com.respec.tacotuesday.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class FullOrder extends Order {
    @Column(name = "fk_order_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    Integer fullOrderId;

    @OneToMany(mappedBy = "fullOrder")
    private List<IndividualOrder> individualOrders;
}
