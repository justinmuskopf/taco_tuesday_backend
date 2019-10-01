package com.muskopf.tacotuesday.bl;

import com.muskopf.tacotuesday.domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    // Employees
    Employee createEmployee(String firstName, String lastName, String nickName);
    Employee createEmployee(Employee employee);
    List<Employee> getAllEmployees();

    // API Keys
    boolean apiKeyExists(String apiKey);
}
