package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.TacoTuesdayTestHelper;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.muskopf.tacotuesday.domain.*;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.TacoResource;
import com.sun.media.sound.SoftTuning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;  // main one
import static org.assertj.core.api.Assertions.in;

@RunWith(SpringRunner.class)
@ComponentScan(basePackages = {
        "com.muskopf.tacotuesday"
})
@SpringBootTest
public class TacoTuesdayResourceMapperTests {
    @MockBean
    private TacoEmailer tacoEmailer;

    @Autowired
    private TacoTuesdayResourceMapper mapper;
    @Autowired
    private TacoTuesdayTestHelper testHelper;

    /**
     * Test mapping of Employee --> EmployeeResource --> Employee
     */
    @Test
    public void test_EmployeeMapping() {
        Employee employee = testHelper.createEmployee();
        EmployeeResource resource = mapper.map(employee);
        Employee mappedBack = mapper.map(resource);

        assertThat(mappedBack)
                .usingRecursiveComparison()
                .ignoringFields(
                        "orders"
                ).isEqualTo(employee);
    }

    /**
     * Test mapping of FullOrder --> FullOrderResource --> FullOrder
     */
    @Test
    public void test_FullOrderMapping() {
        FullOrder fullOrder = testHelper.createFullOrder();
        FullOrderResource resource = mapper.map(fullOrder);
        FullOrder mappedBack = mapper.map(resource);

        assertThat(mappedBack)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(fullOrder);

    }

    /**
     * Tests the mapping of IndividualOrder --> IndividualOrderResource --> IndividualOrder
     */
    @Test
    public void test_IndividualOrderMapping() {
        IndividualOrder individualOrder = testHelper.createIndividualOrder();
        IndividualOrderResource resource = mapper.map(individualOrder);
        IndividualOrder mappedBack = mapper.map(resource);

        assertThat(mappedBack)
                .usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(individualOrder);
    }

    /**
     * Tests the mapping of Taco --> TacoResource --> Taco
     */
    @Test
    public void test_TacoMapping() {
        List<Taco> tacos = testHelper.createTacos();
        List<TacoResource> resources = mapper.mapToTacoResources(tacos);
        List<Taco> mappedBack = resources.stream().map(r -> mapper.map(r)).collect(Collectors.toList());

        assertThat(tacos).containsExactlyInAnyOrderElementsOf(mappedBack);
    }
}
