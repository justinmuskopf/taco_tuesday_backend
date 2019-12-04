package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.TacoTuesdayApiHelper.ApiKeyStatus;
import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.domain.Employee;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.CREATED;
import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

public class TacoTuesdayApiOrderRestControllerTests extends TacoTuesdayBaseRestControllerTester {
    private static String formEndpoint(String path) {
        return "/orders/" + path;
    }

    @Autowired
    private OrderDAO orderDAO;

    /**
     * Test happy path of GET /orders/individual
     */
    @Test
    public void test_getAllIndividualOrders() {
        // Get persisted orders
        List<IndividualOrder> persistedOrders = persistenceHelper.getPersistedIndividualOrders();
        // Map to resources
        List<IndividualOrderResource> expectedResources = mapper.mapIndividualOrdersToIndividualOrderResources(persistedOrders);

        // Perform GET /orders/individual
        List<IndividualOrderResource> responseObject = Arrays.asList(apiHelper.GET(formEndpoint("individual"), OK,
                ApiKeyStatus.EMPTY, IndividualOrderResource[].class));

        expectedResources.forEach(r -> assertThat(responseObject).contains(r));
    }

    /**
     * Test happy path of GET /orders/individual/{order.id}
     */
    @Test
    public void test_getIndividualOrderById() {
        // Get persisted order
        IndividualOrder persistedOrder = persistenceHelper.createIndividualOrder();
        // Map to resource
        IndividualOrderResource expectedResource = mapper.mapIndividualOrderToIndividualOrderResource(persistedOrder);

        // Perform GET /orders/individual/{order.id}
        IndividualOrderResource responseObject = apiHelper.GET(formEndpoint("individual/" + persistedOrder.getId()),
                OK, ApiKeyStatus.EMPTY, IndividualOrderResource.class);

        assertThat(expectedResource).usingRecursiveComparison().isEqualTo(responseObject);
    }

    /**
     * Test happy path of GET /orders/full
     */
    @Test
    public void test_getAllFullOrders() {
        // Get persisted orders
        List<FullOrder> persistedOrders = persistenceHelper.getPersistedFullOrders();
        // Map to resources
        List<FullOrderResource> expectedResources = mapper.mapFullOrdersToFullOrderResources(persistedOrders);

        // Perform GET /orders/individual
        List<FullOrderResource> responseObject = Arrays.asList(apiHelper.GET(formEndpoint("full"), OK,
                ApiKeyStatus.EMPTY, FullOrderResource[].class));

        expectedResources.forEach(r -> assertThat(responseObject).contains(r));
    }

    /**
     * Test happy path of POST /orders/full
     */
    @Test
    public void test_createFullOrder() {
        // Create a full order
        FullOrder order = persistenceHelper.loadObject("POST_FullOrder.json", FullOrder.class);
        order.getIndividualOrders().forEach(io -> {
            Employee e = persistenceHelper.getPersistedEmployeeBySlackId(io.getEmployee().getSlackId());
            io.setEmployee(e);
        });

        // Map to resource
        FullOrderResource expectedResource = mapper.mapFullOrderToFullOrderResource(order);
        List<IndividualOrderResource> expectedIndividualOrders = expectedResource.getIndividualOrders();

        // Perform POST /orders/full
        FullOrderResource responseObject = apiHelper.POST(formEndpoint("full"), CREATED,
                expectedResource, ApiKeyStatus.VALID, FullOrderResource.class);

        // Assert responseObject is properly qualified
        assertThat(responseObject.getId()).isNotNull();
        assertThat(expectedResource).usingRecursiveComparison().ignoringFields(
                "id",
                "createdAt",
                "individualOrders"
        ).isEqualTo(responseObject);

        // Assert that the fullOrderId is set properly on every IO
        // Assert that the expected IOs match the returned ones
        responseObject.getIndividualOrders().forEach(io -> {
            assertThat(io.getFullOrderId()).isEqualTo(responseObject.getId());

            io.setId(null);
            io.setFullOrderId(null);
            io.setCreatedAt(null);

            assertThat(expectedIndividualOrders).contains(io);
        });

        // Get full order from DB and assert that it is correct
        FullOrder persisted = orderDAO.retrieveFullOrder(responseObject.getId());
        assertThat(order).usingRecursiveComparison().ignoringFields(
                "id",
                "createdAt",
                "updatedAt",
                "individualOrders"
        ).isEqualTo(persisted);
    }

    /**
     * Test happy path of GET /orders/full/{order.id}
     */
    @Test
    public void test_getFullOrderById() {
        // Get persisted order
        FullOrder persistedOrder = persistenceHelper.createFullOrder();
        // Map to resource
        FullOrderResource expectedResource = mapper.mapFullOrderToFullOrderResource(persistedOrder);

        // Perform GET /orders/individual/{order.id}
        FullOrderResource responseObject = apiHelper.GET(formEndpoint("full/" + persistedOrder.getId()),
                OK, ApiKeyStatus.EMPTY, FullOrderResource.class);

        assertThat(expectedResource).usingRecursiveComparison().ignoringFields("individualOrders").isEqualTo(responseObject);
        expectedResource.getIndividualOrders().forEach(o -> assertThat(responseObject.getIndividualOrders()).contains(o));
    }
}
