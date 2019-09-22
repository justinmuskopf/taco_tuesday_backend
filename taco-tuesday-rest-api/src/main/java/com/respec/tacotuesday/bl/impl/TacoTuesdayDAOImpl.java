package com.respec.tacotuesday.bl.impl;

import com.respec.tacotuesday.bl.TacoTuesdayDAO;
import com.respec.tacotuesday.bl.repository.EmployeeRepository;
import com.respec.tacotuesday.bl.repository.FullOrderRepository;
import com.respec.tacotuesday.bl.repository.IndividualOrderRepository;
import com.respec.tacotuesday.domain.Employee;
import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@Transactional
public class TacoTuesdayDAOImpl implements TacoTuesdayDAO {
    private EmployeeRepository employeeRepository;
    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;

    @Autowired
    public TacoTuesdayDAOImpl(EmployeeRepository employeeRepository,
                              FullOrderRepository fullOrderRepository,
                              IndividualOrderRepository individualOrderRepository)
    {
        this.employeeRepository = employeeRepository;
        this.fullOrderRepository = fullOrderRepository;
        this.individualOrderRepository = individualOrderRepository;
    }

    private FullOrder getFullOrderIfPresent(Integer id) {
        Optional<FullOrder> optionallyExistingOrder = fullOrderRepository.findById(id);
        return optionallyExistingOrder.orElse(null);
    }

    private IndividualOrder getIndividualOrderIfPresent(Integer id) {
        Optional<IndividualOrder> optionallyExistingOrder = individualOrderRepository.findById(id);
        return optionallyExistingOrder.orElse(null);
    }

    @Override
    public IndividualOrder updateIndividualOrder(IndividualOrder order) {
        IndividualOrder existingOrder = getIndividualOrderIfPresent(order.getId());
        if (existingOrder == null) {
            // TODO Return a BAD REQUEST?
            return null;
        }

        existingOrder.merge(order);

        individualOrderRepository.save(existingOrder);

        return existingOrder;
    }

    @Override
    public FullOrder updateFullOrder(FullOrder order) {
        FullOrder existingOrder = getFullOrderIfPresent(order.getId());
        if (existingOrder == null) {
            // TODO Return a BAD REQUEST?
            return null;
        }

        existingOrder.merge(order);

        fullOrderRepository.save(existingOrder);

        return order;
    }

    @Override
    public IndividualOrder retrieveIndividualOrder(Integer id) {
        return getIndividualOrderIfPresent(id);
    }

    @Override
    public List<IndividualOrder> retrieveAllIndividualOrders() {
        return individualOrderRepository.findAll();
    }

    @Override
    public List<IndividualOrder> retrieveAllIndividualOrdersContainingEmployeeId(Integer employeeId) {
        return individualOrderRepository.findByEmployeeId(employeeId);
    }

    @Override
    public FullOrder retrieveFullOrder(Integer id) {
        return getFullOrderIfPresent(id);
    }

    @Override
    public FullOrder createFullOrder(FullOrder order) {
        return fullOrderRepository.save(order);
    }

    @Override
    public List<FullOrder> retrieveAllFullOrders() {
        return fullOrderRepository.findAll();
    }

    @Override
    public List<FullOrder> retrieveAllFullOrdersContainingEmployeeId(Integer employeeId) {
        return retrieveAllIndividualOrdersContainingEmployeeId(employeeId)
                .stream()
                .map(IndividualOrder::getFullOrder)
                .collect(Collectors.toList());
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
}
