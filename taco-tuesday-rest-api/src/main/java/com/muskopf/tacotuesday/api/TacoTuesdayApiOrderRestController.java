package com.muskopf.tacotuesday.api;

import com.muskopf.tacotuesday.bl.OrderDAO;
import com.muskopf.tacotuesday.bl.proc.ApiKeyValidator;
import com.muskopf.tacotuesday.bl.proc.TacoEmailer;
import com.muskopf.tacotuesday.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/taco-tuesday/v1/orders")
public class TacoTuesdayApiOrderRestController {
    private Logger logger = LoggerFactory.getLogger(TacoTuesdayApiOrderRestController.class);
    private OrderDAO orderDAO;
    private ApiKeyValidator apiKeyValidator;
    private TacoEmailer emailer;

    @Autowired
    public TacoTuesdayApiOrderRestController(OrderDAO orderDAO,
                                        ApiKeyValidator apiKeyValidator,
                                        TacoEmailer emailer)
    {
        this.orderDAO = orderDAO;
        this.apiKeyValidator = apiKeyValidator;
        this.emailer = emailer;
    }

    @GetMapping(value = "/individual")
    public ResponseEntity<List<IndividualOrder>> getAllIndividualOrders() {
        return new ResponseEntity<>(orderDAO.retrieveAllIndividualOrders(), HttpStatus.OK);
    }

    @GetMapping(value = "/individual/{orderId}")
    public ResponseEntity<IndividualOrder> getIndividualOrderByOrderId(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderDAO.retrieveIndividualOrder(orderId), HttpStatus.OK);
    }

    @GetMapping(value = "/full")
    public ResponseEntity<List<FullOrder>> getAllFullOrders() {
        return new ResponseEntity<>(orderDAO.retrieveAllFullOrders(), HttpStatus.OK);
    }

    @PostMapping(value = "/full")
    public ResponseEntity<FullOrder> createFullOrder(@RequestParam(name = "apiKey") String apiKey, @RequestBody FullOrder order) {
        if (apiKeyValidator.isInvalidApiKey(apiKey)) {
            throw new UnrecognizedApiKeyException(apiKey);
        }

        return new ResponseEntity<>(orderDAO.createFullOrder(order), HttpStatus.CREATED);
    }

    @GetMapping(value = "/full/{orderId}")
    public ResponseEntity<FullOrder> getFullOrderByOrderId(@PathVariable Integer orderId) {
        return new ResponseEntity<>(orderDAO.retrieveFullOrder(orderId), HttpStatus.OK);
    }
}
