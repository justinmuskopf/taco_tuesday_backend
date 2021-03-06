package com.muskopf.tacotuesday.bl;

import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;

import java.util.List;
import java.util.Map;

public interface OrderDAO {
    // Individual Orders
    IndividualOrder updateIndividualOrder(IndividualOrder order);

    IndividualOrder retrieveIndividualOrder(Integer id);

    List<IndividualOrder> retrieveAllIndividualOrders();

    List<IndividualOrder> retrieveIndividualOrdersBySlackId(String slackId);

    boolean individualOrderExistsById(Integer id);

    // Full Orders
    FullOrder updateFullOrder(FullOrder order);

    FullOrder retrieveFullOrder(Integer id);

    FullOrder createFullOrder(FullOrder order);

    List<FullOrder> retrieveAllFullOrders();

    List<FullOrder> retrieveFullOrdersBySlackId(String slackId);

    Map<String, Object> retrieveOrderStatistics();

    boolean fullOrderExistsById(Integer id);
}
