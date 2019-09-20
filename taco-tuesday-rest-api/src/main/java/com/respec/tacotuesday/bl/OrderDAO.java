package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderDAO {
    IndividualOrder updateIndividualOrder(IndividualOrder order);
    IndividualOrder retrieveIndividualOrder(Integer id);
    List<IndividualOrder> retrieveAllIndividualOrders();
    List<IndividualOrder> retrieveAllIndividualOrdersContainingEmployeeId(Integer employeeId);
    FullOrder updateFullOrder(FullOrder order);
    FullOrder retrieveFullOrder(Integer id);
    FullOrder createFullOrder(FullOrder order);
    List<FullOrder> retrieveAllFullOrders();
    List<FullOrder> retrieveAllFullOrdersContainingEmployeeId(Integer employeeId);
}
