package com.muskopf.tacotuesday.bl.impl;

import com.muskopf.tacotuesday.bl.EmployeeDAO;
import com.muskopf.tacotuesday.bl.repository.ApiKeyRepository;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import java.util.List;

import static java.util.Objects.isNull;

@Component
public class EmployeeDAOImpl implements EmployeeDAO {
    private EmployeeRepository employeeRepository;
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    public EmployeeDAOImpl(EmployeeRepository employeeRepository, ApiKeyRepository apiKeyRepository)
    {
        this.employeeRepository = employeeRepository;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return createEmployee(
                employee.getFirstName(),
                employee.getLastName(),
                employee.getNickName(),
                employee.getSlackId()
        );
    }

    @Override
    public Employee getEmployeeBySlackId(String slackId) {
        return employeeRepository.findEmployeeBySlackId(slackId);
    }

    @Override
    public Employee createEmployee(String firstName, String lastName, String slackId, String nickName) {
        if (isNull(firstName) || isNull(lastName) || isNull(slackId)) {
            throw new IllegalArgumentException("First name, last name, and Slack ID are required when creating an employee!");
        }

        boolean employeeAlreadyExists = employeeRepository.existsEmployeeByFirstNameAndLastName(firstName, lastName);
        employeeAlreadyExists |= employeeRepository.existsEmployeeBySlackId(slackId);
        if (employeeAlreadyExists) {
            throw new EntityExistsException("The requested employee already exists!");
        }

        Employee employee = new Employee()
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName)
                .slackId(slackId);

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
