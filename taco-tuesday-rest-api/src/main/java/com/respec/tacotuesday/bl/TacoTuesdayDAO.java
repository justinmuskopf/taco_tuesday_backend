package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.domain.ApiKey;
import com.respec.tacotuesday.domain.Employee;
import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TacoTuesdayDAO {
    IndividualOrder updateIndividualOrder(IndividualOrder order);
    IndividualOrder retrieveIndividualOrder(Integer id);
    List<IndividualOrder> retrieveAllIndividualOrders();
    List<IndividualOrder> retrieveAllIndividualOrdersContainingEmployeeId(Integer employeeId);
    FullOrder updateFullOrder(FullOrder order);
    FullOrder retrieveFullOrder(Integer id);
    FullOrder createFullOrder(FullOrder order);
    List<FullOrder> retrieveAllFullOrders();
    List<FullOrder> retrieveAllFullOrdersContainingEmployeeId(Integer employeeId);
    Employee createEmployee(String firstName, String lastName, String nickName);
}
