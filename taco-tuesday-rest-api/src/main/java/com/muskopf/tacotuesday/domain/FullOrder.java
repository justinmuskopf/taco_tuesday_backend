package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

@Entity
public class FullOrder extends Order {
    @JsonIgnore
    @OneToMany(mappedBy = "fullOrder", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JsonManagedReference
    private Set<IndividualOrder> individualOrders;

    public Set<IndividualOrder> getIndividualOrders() { return individualOrders; }
    public void setIndividualOrders(Set<IndividualOrder> individualOrders) { this.individualOrders = individualOrders; }
    public FullOrder individualOrders(Set<IndividualOrder> individualOrders) {
        this.individualOrders = individualOrders;
        return this;
    }
}
