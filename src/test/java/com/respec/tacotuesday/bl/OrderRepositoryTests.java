package com.respec.tacotuesday.bl;

import com.respec.tacotuesday.TacoTuesdayApiApplication;
import com.respec.tacotuesday.domain.FullOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TacoTuesdayApiApplication.class)
public class OrderRepositoryTests {
    @Autowired
    FullOrderRepository repository;

    @Test
    @Transactional
    public void givenFullOrderRepository_saveAndRetrieveFullOrder() {
        FullOrder fullOrder = new FullOrder();
        fullOrder.setBarbacoa(2);
        fullOrder.setBeefFajita(2);

        FullOrder savedOrder = repository.save(fullOrder);
        FullOrder retrievedOrder=  repository.getOne(fullOrder.getId());

        assertThat(savedOrder).isEqualToComparingFieldByFieldRecursively(fullOrder);
        assertThat(savedOrder).isEqualToComparingFieldByFieldRecursively(retrievedOrder);
    }
}
