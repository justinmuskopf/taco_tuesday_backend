package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.stereotype.Component;

@Component
public interface OrderDAO {
    void updateIndividualOrder(IndividualOrder order);
    void updateFullOrder(FullOrder order);
    void retrieveIndividualOrder(Integer id);
    void retrieveFullOrder(Integer id);
    void createFullOrder(FullOrder order);
}
