package com.muskopf.tacotuesday.bl.impl;

import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.repository.EmployeeRepository;
import com.muskopf.tacotuesday.bl.repository.FullOrderRepository;
import com.muskopf.tacotuesday.bl.repository.IndividualOrderRepository;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.TacoType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderDAOImpl implements OrderDAO {
    private FullOrderRepository fullOrderRepository;
    private IndividualOrderRepository individualOrderRepository;
    private EmployeeRepository employeeRepository;
    private EntityManager entityManager;

    @Autowired
    public OrderDAOImpl(FullOrderRepository fullOrderRepository,
                        IndividualOrderRepository individualOrderRepository,
                        EmployeeRepository employeeRepository,
                        EntityManager entityManager)
    {
        this.fullOrderRepository = fullOrderRepository;
        this.individualOrderRepository = individualOrderRepository;
        this.employeeRepository = employeeRepository;
        this.entityManager = entityManager;
    }

    private FullOrder getFullOrderIfPresent(Integer id) {
        return fullOrderRepository.findById(id).orElse(null);
    }

    private IndividualOrder getIndividualOrderIfPresent(Integer id) {
        return individualOrderRepository.findById(id).orElse(null);
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
    @Transactional
    public Map<String, Object> retrieveOrderStatistics() {
        List<TacoType> tacoTypes = Arrays.asList(TacoType.values());
        int numTacoTypes = tacoTypes.size();

        // Create a list of strings e.g. {"SUM(barbacoa)", "SUM(lengua)"}
        List<String> sumStrings = tacoTypes.stream().map(tt -> "SUM(" + tt.columnName() + ")").collect(Collectors.toList());

        // Form SQL query
        String sumString = String.join(", ", sumStrings);
        String sql = String.format("SELECT %s, SUM(total), COUNT(*) from %s", sumString, FullOrder.TABLE_NAME);

        List<Object> result = Arrays.asList((Object[]) entityManager.createNativeQuery(sql).getSingleResult());
        assert result.size() == numTacoTypes + 2; // Length should be #TacoType + 2 for total/count

        // Place all taco counts into a map
        BigInteger totalNumberOfTacos = BigInteger.ZERO;
        Map<TacoType, BigInteger> tacoMap = new HashMap<>();
        for (int i = 0; i < numTacoTypes; i++) {
            BigInteger tacoCount = (BigInteger) result.get(i);
            totalNumberOfTacos = totalNumberOfTacos.add(tacoCount);

            tacoMap.put(tacoTypes.get(i), tacoCount);
        }

        Map<String, Object> orderStatistics = new HashMap<>();
        orderStatistics.put("tacos", tacoMap);
        orderStatistics.put("tacoCount", totalNumberOfTacos);
        orderStatistics.put("total", result.get(numTacoTypes));
        orderStatistics.put("fullOrderCount", result.get(numTacoTypes + 1));
        orderStatistics.put("individualOrderCount", BigInteger.valueOf(individualOrderRepository.count()));

        return orderStatistics;
    }

    @Override
    public boolean fullOrderExistsById(Integer id) {
        return fullOrderRepository.existsById(id);
    }
}
