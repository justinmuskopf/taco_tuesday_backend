package com.muskopf.tacotuesday.bl;

import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;

import java.util.List;

public interface TacoTuesdayDAO {
    // Individual Orders
    IndividualOrder updateIndividualOrder(IndividualOrder order);
    IndividualOrder retrieveIndividualOrder(Integer id);
    List<IndividualOrder> retrieveAllIndividualOrders();
    List<IndividualOrder> retrieveAllIndividualOrdersContainingEmployeeId(Integer employeeId);

    // Full Orders
    FullOrder updateFullOrder(FullOrder order);
    FullOrder retrieveFullOrder(Integer id);
    FullOrder createFullOrder(FullOrder order);
    List<FullOrder> retrieveAllFullOrders();
    List<FullOrder> retrieveAllFullOrdersContainingEmployeeId(Integer employeeId);

    // Employees
    Employee createEmployee(String firstName, String lastName, String nickName);
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();

    // API Keys
    boolean apiKeyExists(String apiKey);
}
