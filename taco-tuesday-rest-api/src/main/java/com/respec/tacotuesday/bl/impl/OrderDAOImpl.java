package com.respec.tacotuesday.bl.impl;

import com.respec.tacotuesday.bl.OrderDAO;
import com.respec.tacotuesday.bl.repository.EmployeeRepository;
import com.respec.tacotuesday.bl.repository.FullOrderRepository;
import com.respec.tacotuesday.bl.repository.IndividualOrderRepository;
import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class OrderDAOImpl implements OrderDAO {
    private EmployeeRepository employeeRepository;
    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;

    @Autowired
    public OrderDAOImpl(EmployeeRepository employeeRepository,
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
}
