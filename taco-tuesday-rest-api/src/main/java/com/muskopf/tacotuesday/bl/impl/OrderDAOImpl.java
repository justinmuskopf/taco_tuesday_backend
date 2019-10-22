package com.muskopf.tacotuesday.bl.impl;

import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.bl.repository.FullOrderRepository;
import com.muskopf.tacotuesday.bl.repository.IndividualOrderRepository;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
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
    public List<IndividualOrder> retrieveAllIndividualOrdersContainingEmployeeId(Integer employeeId) {
        return individualOrderRepository.findByEmployeeId(employeeId);
    }

    @Override
    public FullOrder retrieveFullOrder(Integer id) {
        return getFullOrderIfPresent(id);
    }

    @Override
    public FullOrder createFullOrder(FullOrder order) {
        FullOrder savedOrder = fullOrderRepository.save(order);
        individualOrderRepository.saveAll(order.getIndividualOrders());

        Set<IndividualOrder> individualOrders = savedOrder.getIndividualOrders();
        individualOrders.forEach(o -> o.setFullOrder(savedOrder));

        individualOrderRepository.saveAll(individualOrders);

        return savedOrder;
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
}
