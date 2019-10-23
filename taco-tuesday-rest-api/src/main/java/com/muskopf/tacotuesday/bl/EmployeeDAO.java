package com.muskopf.tacotuesday.bl;

import com.muskopf.tacotuesday.domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    // Employees
	Employee createEmployee(String fullName, String nickName, String slackId);
    Employee createEmployee(Employee employee);
    Employee getEmployeeBySlackId(String slackId);
    List<Employee> getAllEmployees();

    // API Keys
    boolean apiKeyExists(String apiKey);
}
