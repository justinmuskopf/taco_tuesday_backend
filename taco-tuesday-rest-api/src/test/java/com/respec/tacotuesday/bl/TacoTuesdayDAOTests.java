package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.TacoTuesdayApiApplication;
import com.respec.tacotuesday.bl.repository.FullOrderRepository;
import com.respec.tacotuesday.bl.repository.IndividualOrderRepository;
import com.respec.tacotuesday.domain.Employee;
import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import com.respec.tacotuesday.domain.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TacoTuesdayApiApplication.class)
public class TacoTuesdayDAOTests {
    @Autowired
    TacoTuesdayDAO tacoTuesdayDAO;

    @Autowired
    IndividualOrderRepository individualOrderRepository;
    @Autowired
    FullOrderRepository fullOrderRepository;

    private final int NUM_TEST_ORDERS = 5;

    private String randomId() {
        return UUID.randomUUID().toString();
    }

    private Integer randomCount() {
        return new Random().nextInt(10);
    }

    private Employee generateEmployee() {
        return new Employee()
                .firstName(randomId())
                .lastName(randomId())
                .nickName(randomId());
    }

    private IndividualOrder generateIndividualOrder() {
        return (IndividualOrder) new IndividualOrder()
                .employee(generateEmployee())
                .barbacoa(randomCount())
                .beefFajita(randomCount())
                .cabeza(randomCount())
                .carnitas(randomCount())
                .chickenFajita(randomCount())
                .lengua(randomCount())
                .pastor(randomCount())
                .tripa(randomCount());
    }

    private Set<IndividualOrder> generateIndividualOrderList(int n) {
        Set<IndividualOrder> individualOrders = new HashSet<>();
        IntStream.range(0, NUM_TEST_ORDERS).forEach(i -> {
            individualOrders.add(generateIndividualOrder());
        });

        return individualOrders;
    }

    public Integer getTacoSum(Set<IndividualOrder> list, Function<FullOrder, Integer> function) {
        return list.stream().mapToInt(Order::getBarbacoa).sum();
    }

    private FullOrder generateFullOrder() {
        Set<IndividualOrder> individualOrders = generateIndividualOrderList(NUM_TEST_ORDERS);
        FullOrder order = (FullOrder) new FullOrder()
                .barbacoa(getTacoSum(individualOrders, Order::getBarbacoa))
                .beefFajita(getTacoSum(individualOrders, Order::getBeefFajita))
                .cabeza(getTacoSum(individualOrders, Order::getCabeza))
                .carnitas(getTacoSum(individualOrders, Order::getCarnitas))
                .chickenFajita(getTacoSum(individualOrders, Order::getChickenFajita))
                .lengua(getTacoSum(individualOrders, Order::getLengua))
                .pastor(getTacoSum(individualOrders, Order::getPastor))
                .tripa(getTacoSum(individualOrders, Order::getTripa))
                .total(100F);

        individualOrders.forEach(o -> o.setFullOrder(order));
        order.setIndividualOrders(individualOrders);

        return order;
    }

    private List<FullOrder> generateFullOrderList(int n) {
        List<FullOrder> fullOrders = new ArrayList<>();
        IntStream.range(0, n).forEach(i -> {
            fullOrders.add(generateFullOrder());
        });

        return fullOrders;
    }

    @Test
    public void test_retrieveAllIndividualOrders() {
        Set<IndividualOrder> individualOrders = generateIndividualOrderList(NUM_TEST_ORDERS);

        List<IndividualOrder> savedOrders = individualOrderRepository.saveAll(individualOrders);
        assertThat(savedOrders.equals(individualOrders));

        List<IndividualOrder> retrievedOrders = tacoTuesdayDAO.retrieveAllIndividualOrders();
        assertThat(retrievedOrders.equals(individualOrders));
    }

    @Test
    public void test_retrieveAllFullOrders() {
        List<FullOrder> fullOrders = generateFullOrderList(NUM_TEST_ORDERS);

        List<FullOrder> savedOrders = fullOrderRepository.saveAll(fullOrders);
        assertThat(savedOrders).isEqualTo(fullOrders);

        List<FullOrder> retrievedOrders = tacoTuesdayDAO.retrieveAllFullOrders();
        assertThat(retrievedOrders).isEqualTo(fullOrders);

    }

    @Test
    public void test_retrieveFullOrdersByEmployeeId() {
        //List<FullOrder> fullOrders = generateFullOrderList(NUM_TEST_ORDERS);
        //Integer firstEmployeeId = fullOrders.get(0).getIndividualOrders().get(0).getEmployeeId();

    }
}
