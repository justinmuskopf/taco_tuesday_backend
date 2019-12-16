package com.muskopf.tacotuesday.bl.impl;

import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.bl.repository.FullOrderRepository;
import com.muskopf.tacotuesday.bl.repository.IndividualOrderRepository;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderDAOImpl implements OrderDAO {
    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;
    private EmployeeRepository employeeRepository;

    @Autowired
    public OrderDAOImpl(FullOrderRepository fullOrderRepository,
                        IndividualOrderRepository individualOrderRepository,
                        EmployeeRepository employeeRepository)
    {
        this.fullOrderRepository = fullOrderRepository;
        this.individualOrderRepository = individualOrderRepository;
        this.employeeRepository = employeeRepository;
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
    public List<IndividualOrder> retrieveIndividualOrdersBySlackId(String slackId) {
        return individualOrderRepository.findByEmployeeSlackId(slackId);
    }

    @Override
    public boolean individualOrderExistsById(Integer id) {
        return individualOrderRepository.existsById(id);
    }

    @Override
    public FullOrder retrieveFullOrder(Integer id) {
        return getFullOrderIfPresent(id);
    }

    @Override
    public FullOrder createFullOrder(FullOrder order) {
        // Persist all individual orders' employees
        Set<IndividualOrder> individualOrders = order.getIndividualOrders();
        for (IndividualOrder individualOrder : individualOrders) {
            Employee employee = individualOrder.getEmployee();
            String slackId = employee.getSlackId();

            if (employeeRepository.existsEmployeeBySlackId(slackId)) {
                employee.setId(employeeRepository.findBySlackId(slackId).getId());
            }

            individualOrder.setEmployee(employeeRepository.save(employee));
        }

        // Persist individual orders
        Set<IndividualOrder> persistedIndividualOrders = new HashSet<>(individualOrderRepository.saveAll(individualOrders));
        order.setIndividualOrders(persistedIndividualOrders);

        // Persist full order and set the individual orders' reference to it
        FullOrder savedOrder = fullOrderRepository.save(order);
        persistedIndividualOrders.forEach(o -> o.setFullOrder(savedOrder));

        // Persist all individual orders
        individualOrderRepository.saveAll(persistedIndividualOrders);

        return savedOrder;
    }

    @Override
    public List<FullOrder> retrieveAllFullOrders() {
        return fullOrderRepository.findAll();
    }

    @Override
    @Transactional
    public List<FullOrder> retrieveFullOrdersBySlackId(String slackId) {
        List<IndividualOrder> individualOrders = retrieveIndividualOrdersBySlackId(slackId);

        return individualOrders
                .stream()
                .map(o -> (FullOrder) Hibernate.unproxy(o.getFullOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean fullOrderExistsById(Integer id) {
        return fullOrderRepository.existsById(id);
    }
}
