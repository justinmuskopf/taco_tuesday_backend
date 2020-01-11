package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.api.validator.*;
import com.muskopf.tacotuesday.api.validator.OrderIdValidator.OrderType;
import com.muskopf.tacotuesday.api.validator.SlackIdValidator.SlackIdType;
import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.DomainObject;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.domain.TacoType;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import com.muskopf.tacotuesday.resource.OrderSummaryResource;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Validated
@RestController
@CacheConfig(cacheNames = "orders")
@RequestMapping(value = "/taco-tuesday/v1/orders")
public class TacoTuesdayApiOrderRestController {
    private OrderDAO orderDAO;
    private TacoTuesdayResourceMapper mapper;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiOrderRestController(OrderDAO orderDAO,
                                             TacoTuesdayResourceMapper mapper,
                                             TacoEmailer emailer)
    {
        this.orderDAO = orderDAO;
        this.mapper = mapper;
        this.emailer = emailer;
    }

    @CrossOrigin
    @ApiOperation(value = "Retrieve statistics about orders")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = OrderSummaryResource.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @Cacheable
    @GetMapping(value = "/summary")
    public ResponseEntity<OrderSummaryResource> getOrderSummary() {
        OrderSummaryResource summaryResource = new OrderSummaryResource();

        Map<String, Object> orderStatistics = orderDAO.retrieveOrderStatistics();
        summaryResource.setFullOrderCount((BigInteger) orderStatistics.get("fullOrderCount"));
        summaryResource.setIndividualOrderCount((BigInteger) orderStatistics.get("individualOrderCount"));
        summaryResource.setTacoCount((BigInteger) orderStatistics.get("tacoCount"));
        summaryResource.setTacos((Map<TacoType, BigInteger>) orderStatistics.get("tacos"));
        summaryResource.setTotal((Double) orderStatistics.get("total"));

        return new ResponseEntity<>(summaryResource, HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Retrieve all Individual Orders")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = IndividualOrderResource[].class),
            @ApiResponse(code = 400, message = "Invalid Slack ID Provided", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @GetMapping(value = "/individual")
    public ResponseEntity<List<IndividualOrderResource>> getAllIndividualOrders(@RequestHeader(name = "slackId", required = false)
                                                                                    @SlackId(type = SlackIdType.Optional) String slackId,
                                                                                @RequestHeader(name = "sortByDate", required = false)
                                                                                    boolean sortByDate)
    {
        log.info("GET /orders/individual");

        List<IndividualOrder> orders = isEmpty(slackId) ?
                orderDAO.retrieveAllIndividualOrders() :
                orderDAO.retrieveIndividualOrdersBySlackId(slackId);

        if (sortByDate)
            orders.sort(Comparator.comparing(DomainObject::getCreatedAt));

        return new ResponseEntity<>(mapper.mapIndividualOrdersToIndividualOrderResources(orders), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Retrieve an Individual Order by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = IndividualOrder.class),
            @ApiResponse(code = 404, message = "No Such Order Exists", response = IndividualOrder.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @Cacheable
    @GetMapping(value = "/individual/{orderId}")
    public ResponseEntity<IndividualOrderResource> getIndividualOrderByOrderId(@PathVariable
                                                                                   @OrderId(type = OrderType.Individual) Integer orderId)
    {
        log.info("GET /orders/individual/{orderId}, Order ID: " + orderId);

        IndividualOrder order = orderDAO.retrieveIndividualOrder(orderId);

        return new ResponseEntity<>(mapper.mapIndividualOrderToIndividualOrderResource(order), HttpStatus.OK);
    }

    @CrossOrigin
    @ApiOperation(value = "Retrieve all Full Orders")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = IndividualOrderResource[].class),
            @ApiResponse(code = 400, message = "Invalid Slack ID Provided", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @Cacheable
    @GetMapping(value = "/full")
    public ResponseEntity<List<FullOrderResource>> getAllFullOrders(@RequestHeader(name = "slackId", required = false)
                                                                        @SlackId(type = SlackIdType.Optional) String slackId,
                                                                    @RequestHeader(name = "sortByDate", required = false)
                                                                        boolean sortByDate)
    {
        log.info("GET /orders/full");

        List<FullOrder> orders = isEmpty(slackId) ? orderDAO.retrieveAllFullOrders() : orderDAO.retrieveFullOrdersBySlackId(slackId);

        if (sortByDate)
            orders.sort(Comparator.comparing(DomainObject::getCreatedAt));

        return new ResponseEntity<>(mapper.mapFullOrdersToFullOrderResources(orders), HttpStatus.OK);
    }

    @ApiOperation(value = "Create a new Full Order")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Full Order Created", response = FullOrderResource.class),
            @ApiResponse(code = 400, message = "Invalid Request Body", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 401, message = "Invalid API Key", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @CachePut
    @PostMapping(value = "/full")
    public ResponseEntity<FullOrderResource> createFullOrder(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                             @RequestBody @Valid FullOrderResource orderResource)
    {
        log.info("POST /orders/full");

        FullOrder order = mapper.mapFullOrderResourceToFullOrder(orderResource);
        order = orderDAO.createFullOrder(order);

        emailer.sendOrderSubmittedEmail(order);

        return new ResponseEntity<>(mapper.mapFullOrderToFullOrderResource(order), HttpStatus.CREATED);
    }

    @CrossOrigin
    @ApiOperation(value = "Retrieve a Full Order by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved Successfully", response = FullOrderResource.class),
            @ApiResponse(code = 404, message = "No Such Full Order Exists", response = TacoTuesdayExceptionResponse.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = TacoTuesdayExceptionResponse.class)
    })
    @Cacheable
    @GetMapping(value = "/full/{orderId}")
    public ResponseEntity<FullOrderResource> getFullOrderByOrderId(@PathVariable
                                                                       @OrderId(type = OrderType.Full) Integer orderId)
    {
        log.info("GET /orders/full/{orderId}, Order ID: " + orderId);

        FullOrder order = orderDAO.retrieveFullOrder(orderId);

        return new ResponseEntity<>(mapper.mapFullOrderToFullOrderResource(order), HttpStatus.OK);
    }
}
