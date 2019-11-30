package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.proc.ApiKeyValidator;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.bl.proc.TacoTuesdayResourceMapper;
import com.muskopf.tacotuesday.domain.FullOrder;
import com.muskopf.tacotuesday.domain.IndividualOrder;
import com.muskopf.tacotuesday.resource.FullOrderResource;
import com.muskopf.tacotuesday.resource.IndividualOrderResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/orders")
public class TacoTuesdayApiOrderRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiOrderRestController.class);
    private OrderDAO orderDAO;
    private ApiKeyValidator apiKeyValidator;
    private TacoTuesdayResourceMapper mapper;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiOrderRestController(OrderDAO orderDAO,
                                             ApiKeyValidator apiKeyValidator,
                                             TacoTuesdayResourceMapper mapper,
                                             TacoEmailer emailer)
    {
        this.orderDAO = orderDAO;
        this.apiKeyValidator = apiKeyValidator;
        this.mapper = mapper;
        this.emailer = emailer;
    }

    @GetMapping(value = "/individual")
    public ResponseEntity<List<IndividualOrderResource>> getAllIndividualOrders(@RequestHeader(name = "slackId", required = false) String slackId) {
        List<IndividualOrder> orders = isEmpty(slackId) ? orderDAO.retrieveAllIndividualOrders() : orderDAO.retrieveIndividualOrdersBySlackId(slackId);

        return new ResponseEntity<>(mapper.mapToIndividualOrderResources(orders), HttpStatus.OK);
    }

    @GetMapping(value = "/individual/{orderId}")
    public ResponseEntity<IndividualOrderResource> getIndividualOrderByOrderId(@PathVariable @NotEmpty Integer orderId) {
        IndividualOrder order = orderDAO.retrieveIndividualOrder(orderId);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.OK);
    }

    @GetMapping(value = "/full")
    public ResponseEntity<List<FullOrderResource>> getAllFullOrders(@RequestHeader(name = "slackId", required = false) String slackId) {
        List<FullOrder> orders = isEmpty(slackId) ? orderDAO.retrieveAllFullOrders() : orderDAO.retrieveFullOrdersBySlackId(slackId);

        return new ResponseEntity<>(mapper.mapToFullOrderResources(orders), HttpStatus.OK);
    }

    @PostMapping(value = "/full")
    public ResponseEntity<FullOrderResource> createFullOrder(@RequestParam(name = "apiKey") @NotEmpty String apiKey,
                                                             @RequestBody @NotEmpty FullOrderResource orderResource)
    {
        if (apiKeyValidator.isInvalidApiKey(apiKey)) {
            throw new UnrecognizedApiKeyException(apiKey);
        }

        FullOrder order = mapper.map(orderResource);
        order = orderDAO.createFullOrder(order);

        emailer.sendOrderSubmittedEmail(order);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.CREATED);
    }

    @GetMapping(value = "/full/{orderId}")
    public ResponseEntity<FullOrderResource> getFullOrderByOrderId(@PathVariable @NotEmpty Integer orderId) {
        logger.info("Getting FullOrder by ID: " + orderId);
        FullOrder order = orderDAO.retrieveFullOrder(orderId);

        return new ResponseEntity<>(mapper.map(order), HttpStatus.OK);
    }
}
