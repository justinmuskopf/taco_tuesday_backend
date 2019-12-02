package com.muskopf.tacotuesday.bl;

import com.muskopf.tacotuesday.domain.Employee;

import java.util.List;

public interface EmployeeDAO {
    // Employees
    Employee createEmployee(String fullName, String nickName, String slackId, boolean isAdmin);

    Employee createEmployee(String fullName, String slackId, boolean isAdmin);

    Employee createEmployee(String fullName, String slackId);

    Employee createEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    List<Employee> updateEmployees(List<Employee> employees);

    Employee getEmployeeBySlackId(String slackId);

    Employee registerEmployeeApiKey(Employee employee);

    List<Employee> getAllEmployees();

    boolean employeeExistsBySlackId(String slackId);

    // API Keys
    boolean apiKeyExists(String apiKey);
}
