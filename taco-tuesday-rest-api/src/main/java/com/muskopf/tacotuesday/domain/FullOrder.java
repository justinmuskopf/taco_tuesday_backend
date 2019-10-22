package com.muskopf.tacotuesday.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
public class FullOrder extends Order {
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
