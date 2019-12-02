package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.api.validator.ApiKey;
import com.muskopf.tacotuesday.api.validator.OrderId;
import com.muskopf.tacotuesday.api.validator.OrderIdValidator;
import com.muskopf.tacotuesday.api.validator.OrderIdValidator.OrderType;
import com.muskopf.tacotuesday.api.validator.SlackId;
import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Validated
@RestController
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

    @GetMapping(value = "/individual")
    public ResponseEntity<List<IndividualOrderResource>> getAllIndividualOrders(@RequestHeader(name = "slackId", required = false)
                                                                                    @SlackId(required = false) String slackId)
    {
        log.info("GET /orders/individual");

        List<IndividualOrder> orders = isEmpty(slackId) ?
                orderDAO.retrieveAllIndividualOrders() :
                orderDAO.retrieveIndividualOrdersBySlackId(slackId);

        return new ResponseEntity<>(mapper.mapToIndividualOrderResources(orders), HttpStatus.OK);
    }

    @GetMapping(value = "/individual/{orderId}")
    public ResponseEntity<IndividualOrderResource> getIndividualOrderByOrderId(@PathVariable
                                                                                   @OrderId(type = OrderType.Individual) Integer orderId)
    {
        log.info("GET /orders/individual/{orderId}, Order ID: " + orderId);

        IndividualOrder order = orderDAO.retrieveIndividualOrder(orderId);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.OK);
    }

    @GetMapping(value = "/full")
    public ResponseEntity<List<FullOrderResource>> getAllFullOrders(@RequestHeader(name = "slackId", required = false)
                                                                        @SlackId(required = false) String slackId)
    {
        log.info("GET /orders/full");

        List<FullOrder> orders = isEmpty(slackId) ? orderDAO.retrieveAllFullOrders() : orderDAO.retrieveFullOrdersBySlackId(slackId);

        return new ResponseEntity<>(mapper.mapToFullOrderResources(orders), HttpStatus.OK);
    }

    @PostMapping(value = "/full")
    public ResponseEntity<FullOrderResource> createFullOrder(@RequestParam(name = "apiKey") @ApiKey String apiKey,
                                                             @RequestBody @Valid FullOrderResource orderResource)
    {
        log.info("POST /orders/full");

        FullOrder order = mapper.map(orderResource);
        order = orderDAO.createFullOrder(order);

        emailer.sendOrderSubmittedEmail(order);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.CREATED);
    }

    @GetMapping(value = "/full/{orderId}")
    public ResponseEntity<FullOrderResource> getFullOrderByOrderId(@PathVariable @OrderId(type = OrderType.Full) Integer orderId) {
        log.info("GET /orders/full/{orderId}, Order ID: " + orderId);

        FullOrder order = orderDAO.retrieveFullOrder(orderId);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.OK);
    }
}
