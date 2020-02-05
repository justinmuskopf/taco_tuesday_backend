package com.muskopf.tacotuesday.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muskopf.tacotuesday.TacoTuesdayApiHelper;
import com.muskopf.tacotuesday.TacoTuesdayApiHelper.ApiKeyStatus;
import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.domain.*;
import com.muskopf.tacotuesday.resource.EmployeeResource;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.OrderSummaryResource;
import net.bytebuddy.dynamic.scaffold.MethodGraph;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.muskopf.tacotuesday.TacoTuesdayApiHelper.ResponseStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class TacoTuesdayApiOrderRestControllerTests extends TacoTuesdayBaseRestControllerTester {
    private static String formEndpoint(String path) {
        return "/orders/" + path;
    }

    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test happy path of GET /orders/summary
     */
    @Test
    public void test_getOrderSummary() {
        List<FullOrder> persistedFullOrders = persistenceHelper.getPersistedFullOrders();
        List<IndividualOrder> persistedIndividualOrders = persistenceHelper.getPersistedIndividualOrders();

        Integer expectedTotalNumberOfTacos = persistedFullOrders.stream().mapToInt(this::tacoCountFromOrder).sum();
        Double expectedTotal = persistedFullOrders.stream().mapToDouble(FullOrder::getTotal).sum();

        OrderSummaryResource summaryResource = apiHelper.GET(formEndpoint("summary"), OK, ApiKeyStatus.EMPTY, OrderSummaryResource.class);
        assertThat(summaryResource.getFullOrderCount()).isEqualTo(persistedFullOrders.size());
        assertThat(summaryResource.getIndividualOrderCount()).isEqualTo(persistedIndividualOrders.size());
        assertThat(summaryResource.getTacoCount().toString()).isEqualTo(expectedTotalNumberOfTacos.toString());
        assertThat(summaryResource.getTotal()).isEqualTo(expectedTotal);
    }

    /**
     * Test happy path of GET /orders/summary
     */
    @Test
    public void test_getOrderSummary_afterExtraSubmit() {
        List<FullOrder> fullOrders = persistenceHelper.getPersistedFullOrders();

        test_getOrderSummary();
        FullOrder order = new FullOrder();
        IndividualOrder individualOrder = new IndividualOrder();

        Employee employee = persistenceHelper.getPersistedEmployees().get(0);
        individualOrder.setEmployee(employee);
        individualOrder.setBarbacoa(2);
        individualOrder.setTotal((float) 10.0);

        order.setIndividualOrders(Collections.singleton(individualOrder));
        order.setTotal((float) 10.0);


        FullOrderResource createdResource = apiHelper.POST(formEndpoint("full"), CREATED, mapper.mapFullOrderToFullOrderResource(order), ApiKeyStatus.VALID, FullOrderResource.class);
        OrderSummaryResource summaryResource = apiHelper.GET(formEndpoint("summary"), OK, ApiKeyStatus.EMPTY, OrderSummaryResource.class);


        assertThat(summaryResource.getFullOrderCount()).isEqualTo(fullOrders.size() + 1);
    }

    /**
     * Test happy path of GET /orders/individual
     */
    @Test
    public void test_getAllIndividualOrders_happy() {
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
     * Test happy path of GET /orders/individual with the sort header as true
     */
    @Test
    public void test_getAllIndividualOrdersSorted_happy() {
        // Get persisted orders
        List<IndividualOrder> persistedOrders = persistenceHelper.getPersistedIndividualOrders();
        // Map to resources
        List<IndividualOrderResource> expectedResources = mapper.mapIndividualOrdersToIndividualOrderResources(persistedOrders);

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.put("sortByDate", Collections.singletonList(Boolean.toString(true)));

        // Perform GET /orders/individual
        List<IndividualOrderResource> responseObject = Arrays.asList(apiHelper.GET(formEndpoint("individual"), new HttpHeaders(headerMap), OK,
                ApiKeyStatus.EMPTY, IndividualOrderResource[].class));

        expectedResources.forEach(r -> assertThat(responseObject).contains(r));

        // Assert sorted
        ListIterator<IndividualOrderResource> resourceListIterator = responseObject.listIterator();
        IndividualOrderResource currentResource = resourceListIterator.next();
        while (resourceListIterator.hasNext()) {
            IndividualOrderResource next = resourceListIterator.next();
            assertThat(next.getCreatedAt()).isAfterOrEqualTo(currentResource.getCreatedAt());

            currentResource = next;
        }

    }

    /**
     * Test happy path of GET /orders/individual where slackId header is provided
     */
    @Test
    public void test_getAllIndividualOrdersIncludingSlackId_happy() {
        Employee employee = persistenceHelper.getPersistedEmployees().get(0);
        String slackId = employee.getSlackId();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.put("slackId", Collections.singletonList(slackId));

        List<Integer> individualOrderIdsInWhichEmployeeParticipated = persistenceHelper.getPersistedIndividualOrders()
                .stream()
                .filter(o -> o.getEmployee().getSlackId().equals(slackId))
                .map(DomainObject::getId)
                .collect(Collectors.toList());

        List<IndividualOrderResource> individualOrderResources = Arrays.asList(apiHelper.GET(formEndpoint("individual"), new HttpHeaders(headerMap), OK,
                ApiKeyStatus.EMPTY, IndividualOrderResource[].class));

        assertThat(individualOrderResources.size()).isEqualTo(individualOrderIdsInWhichEmployeeParticipated.size());
        individualOrderResources.forEach(o -> assertThat(individualOrderIdsInWhichEmployeeParticipated.contains(o.getId())));
    }

    /**
     * Test happy path of GET /orders/individual/{order.id}
     */
    @Test
    public void test_getIndividualOrderById_happy() {
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
     * Test sad path of GET /orders/individual/{orderId} where orderId is invalid
     */
    @Test
    public void test_getIndividualOrderById_sad() {
        Integer invalidId = 1313131313;

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(formEndpoint("individual/" + invalidId),
                NOT_FOUND, ApiKeyStatus.EMPTY, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.NOT_FOUND, false,
                new String[]{"Invalid IndividualOrder ID: " + invalidId});
    }

    /**
     * Test happy path of GET /orders/full
     */
    @Test
    public void test_getAllFullOrders_happy() {
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
     * Test happy path of GET /orders/full with the sort header as true
     */
    @Test
    public void test_getAllFullOrdersSortedByDate_happy() {
        // Get persisted orders
        List<FullOrder> persistedOrders = persistenceHelper.getPersistedFullOrders();
        // Map to resources
        List<FullOrderResource> expectedResources = mapper.mapFullOrdersToFullOrderResources(persistedOrders);

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.put("sortByDate", Collections.singletonList(Boolean.toString(true)));

        // Perform GET /orders/individual
        List<FullOrderResource> responseObject = Arrays.asList(apiHelper.GET(formEndpoint("full"), new HttpHeaders(headerMap), OK,
                ApiKeyStatus.EMPTY, FullOrderResource[].class));

        expectedResources.forEach(r -> assertThat(responseObject).contains(r));

        // Assert sorted
        ListIterator<FullOrderResource> resourceListIterator = responseObject.listIterator();
        FullOrderResource currentResource = resourceListIterator.next();
        while (resourceListIterator.hasNext()) {
            FullOrderResource next = resourceListIterator.next();
            assertThat(next.getCreatedAt()).isAfterOrEqualTo(currentResource.getCreatedAt());

            currentResource = next;
        }
    }

    /**
     * Test happy path of GET /orders/full where slackId header is provided
     */
    @Test
    public void test_getAllFullOrdersIncludingSlackId_happy() {
        Employee employee = persistenceHelper.getPersistedEmployees().get(0);
        String slackId = employee.getSlackId();

        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.put("slackId", Collections.singletonList(slackId));

        List<Integer> fullOrderIdsInWhichEmployeeParticipated = persistenceHelper.getPersistedIndividualOrders()
                .stream()
                .filter(o -> o.getEmployee().getSlackId().equals(slackId))
                .map(o -> o.getFullOrder().getId())
                .collect(Collectors.toList());

        List<FullOrderResource> fullOrderResources = Arrays.asList(apiHelper.GET(formEndpoint("full"), new HttpHeaders(headerMap), OK,
                ApiKeyStatus.EMPTY, FullOrderResource[].class));


        assertThat(fullOrderResources.size()).isEqualTo(fullOrderIdsInWhichEmployeeParticipated.size());
        fullOrderResources.forEach(o -> assertThat(fullOrderIdsInWhichEmployeeParticipated.contains(o.getId())));
    }

    /**
     * Test sad path of GET /orders/full/{orderId} where orderId is invalid
     */
    @Test
    public void test_getFullOrderById_sad() {
        Integer invalidId = 1313131313;

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.GET(formEndpoint("full/" + invalidId),
                NOT_FOUND, ApiKeyStatus.EMPTY, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.NOT_FOUND, false,
                new String[]{"Invalid FullOrder ID: " + invalidId});

    }

    /**
     * Test happy path of POST /orders/full
     */
    @Test
    public void test_createFullOrder_happy() {
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
     * Test sad path of POST /orders/full where individualOrders is empty
     */
    @Test
    public void test_postFullOrders_sad_noIndividualOrders() {
        FullOrder fullOrder = persistenceHelper.createFullOrder();
        fullOrder.setIndividualOrders(new HashSet<>());

        FullOrderResource resource = mapper.mapFullOrderToFullOrderResource(fullOrder);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(formEndpoint("full"), BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false,
                new String[] {"A full order must have individual orders!"});
    }

    /**
     * Test sad path of POST /orders/full where a taco count is negative
     */
    @Test
    public void test_postFullOrders_sad_negativeTacoCount() throws JsonProcessingException {
        FullOrder fullOrder = persistenceHelper.createFullOrder();
        fullOrder.setBarbacoa(-42);

        FullOrderResource resource = mapper.mapFullOrderToFullOrderResource(fullOrder);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(formEndpoint("full"), BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false,
                new String[] {"Invalid Taco Order: " + resource.getTacos().toString()});
    }

    /**
     * Test sad path of POST /orders/full where a taco count is negative
     */
    @Test
    public void test_postFullOrders_sad_negativePrice() {
        FullOrder fullOrder = persistenceHelper.createFullOrder();
        fullOrder.setTotal((float) -1);

        FullOrderResource resource = mapper.mapFullOrderToFullOrderResource(fullOrder);

        TacoTuesdayExceptionResponseResource responseObject = apiHelper.POST(formEndpoint("full"), BAD_REQUEST, resource,
                ApiKeyStatus.VALID, TacoTuesdayExceptionResponseResource.class);

        apiHelper.assertExceptionResponseIsValid(responseObject, HttpStatus.BAD_REQUEST, false,
                new String[] {"Invalid Price: -1.0"});
    }

    /**
     * Test happy path of GET /orders/full/{order.id}
     */
    @Test
    public void test_getFullOrderById_happy() {
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

    private Integer tacoCountFromOrder(Order order) {
        Integer tacoCount = 0;
        for (TacoType tacoType : TacoType.values()) {
            tacoCount += order.getTacoCount(tacoType);
        }

        return tacoCount;
    }
}
