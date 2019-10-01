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
        return createEmployee(employee.getFirstName(), employee.getLastName(), employee.getNickName());
    }

    @Override
    public Employee createEmployee(String firstName, String lastName, String nickName) {
        if (isNull(firstName) || isNull(lastName)) {
            throw new IllegalArgumentException("Both first name and last name are required when creating an employee!");
        }

        boolean employeeAlreadyExists = employeeRepository.existsEmployeeByFirstNameAndLastName(firstName, lastName);
        if (employeeAlreadyExists) {
            throw new EntityExistsException("The requested employee already exists!");
        }

        Employee employee = new Employee()
                .firstName(firstName)
                .lastName(lastName)
                .nickName(nickName);

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
