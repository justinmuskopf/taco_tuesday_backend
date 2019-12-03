package com.muskopf.tacotuesday.bl.proc;

import com.muskopf.tacotuesday.TacoTuesdayPersistenceInitializer;
import com.muskopf.tacotuesday.config.TacoTuesdayApiConfiguration;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.Taco;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.TacoResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = {TacoTuesdayApiConfiguration.class})
public class TacoTuesdayResourceMapperTests {
    @MockBean
    private TacoEmailer tacoEmailer;

    @Autowired
    private TacoTuesdayResourceMapper mapper;
    @Autowired
    private TacoTuesdayPersistenceInitializer testHelper;

    /**
     * Test mapping of Employee --> EmployeeResource --> Employee
     */
    @Test
    public void test_EmployeeMapping() {
        Employee employee = testHelper.createEmployee();
        EmployeeResource resource = mapper.mapToEmployeeResource(employee);
        Employee mappedBack = mapper.mapToEmployee(resource);

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
        FullOrderResource resource = mapper.mapToFullOrderResource(fullOrder);
        FullOrder mappedBack = mapper.mapToFullOrder(resource);

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
        IndividualOrderResource resource = mapper.mapToIndividualOrderResource(individualOrder);
        IndividualOrder mappedBack = mapper.mapToIndividualOrder(resource);

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
        List<Taco> tacos = testHelper.loadObjects(Taco.class);
        List<TacoResource> resources = mapper.mapToTacoResources(tacos);
        List<Taco> mappedBack = resources.stream().map(r -> mapper.mapToTaco(r)).collect(Collectors.toList());

        assertThat(tacos).containsExactlyInAnyOrderElementsOf(mappedBack);
    }
}
