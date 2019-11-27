package com.muskopf.tacotuesday.bl.impl;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.proc.ApiKeyGenerator;
import com.muskopf.tacotuesday.bl.repository.ApiKeyRepository;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.domain.ApiKey;
import com.muskopf.tacotuesday.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import static reactor.util.StringUtils.isEmpty;

@Component
public class EmployeeDAOImpl implements EmployeeDAO {
    private EmployeeRepository employeeRepository;
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    public EmployeeDAOImpl(EmployeeRepository employeeRepository, ApiKeyRepository apiKeyRepository) {
        this.employeeRepository = employeeRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return createEmployee(
                employee.getFullName(),
                employee.getSlackId(),
                employee.getNickName(),
                employee.isAdmin()
        );
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        String slackId = employee.getSlackId();
        if (isEmpty(slackId)) {
            throw new ValidationException("No Slack ID provided for employee!");
        }

        Employee existingEmployee = employeeRepository.findBySlackId(slackId);
        if (existingEmployee == null) {
            throw new ValidationException("No existing employee with Slack ID " + slackId + "!");
        }

        existingEmployee.merge(employee);
        existingEmployee = employeeRepository.save(existingEmployee);

        return existingEmployee;
    }

    @Override
    public List<Employee> updateEmployees(List<Employee> employees) {
        Map<String, Employee> employeesBySlackId = new HashMap<>();
        employees.forEach(e -> employeesBySlackId.put(e.getSlackId(), e));

        List<Employee> existingEmployees = employeeRepository.findBySlackIdIn(employeesBySlackId.keySet());
        for (Employee employee : existingEmployees) {
            employee.merge(employeesBySlackId.get(employee.getSlackId()));
        }

        existingEmployees = employeeRepository.saveAll(existingEmployees);

        return existingEmployees;
    }

    @Override
    public Employee createEmployee(String fullName, String slackId) {
        return createEmployee(fullName, slackId, false);
    }

    @Override
    public Employee createEmployee(String fullName, String slackId, boolean isAdmin) {
        return createEmployee(fullName, slackId, null, isAdmin);
    }

    @Override
    public Employee getEmployeeBySlackId(String slackId) {
        return employeeRepository.findBySlackId(slackId);
    }

    @Override
    public Employee registerEmployeeApiKey(Employee employee) {
        ApiKey apiKey = ApiKeyGenerator.generateForEmployee(employee);
        apiKey = apiKeyRepository.save(apiKey);

        employee.setApiKey(apiKey);

        return employee;
    }

    @Override
    public Employee createEmployee(String fullName, String slackId, String nickName, boolean isAdmin) {
        if (isNull(fullName) || isNull(slackId)) {
            throw new IllegalArgumentException("Full Name and Slack ID are required when creating an employee!");
        }

        if (employeeRepository.existsEmployeeBySlackId(slackId)) {
            throw new EntityExistsException("The requested employee already exists!");
        }

        Employee employee = new Employee();
        employee.setFullName(fullName);
        employee.setNickName(nickName);
        employee.setSlackId(slackId);
        employee.setAdmin(isAdmin);

        registerEmployeeApiKey(employee);

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public boolean apiKeyExists(String apiKey) {
        return apiKeyRepository.existsByKey(apiKey);
    }
}
