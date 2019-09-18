package com.respec.tacotuesday.api;

import com.respec.tacotuesday.domain.FullOrder;
import com.respec.tacotuesday.domain.IndividualOrder;
import com.respec.tacotuesday.domain.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/taco-tuesday/v1")
public class TacoTuesdayApiRestController {

    @RequestMapping(value = "/orders/full/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<FullOrder> getFullOrderByOrderId(@PathVariable Integer orderId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/individual/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<IndividualOrder> getIndividualOrderByOrderId(@PathVariable Integer orderId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/full", method = RequestMethod.GET)
    public ResponseEntity<List<FullOrder>> getAllFullOrders() {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/individual", method = RequestMethod.GET)
    public ResponseEntity<List<IndividualOrder>> getAllIndividualOrders() {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/individual/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<List<IndividualOrder>> getAllIndiviudalOrdersWithEmployeeId(@PathVariable Integer employeeId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/full/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<List<FullOrder>> getAllFullOrdersWithEmployeeId(@PathVariable Integer employeeId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/orders/{employeeId}", method = RequestMethod.GET)
    public ResponseEntity<List<Order>> getAllOrdersByEmployeeId(@PathVariable Integer employeeId) {
        return new ResponseEntity<>(null, HttpStatus.NOT_IMPLEMENTED);
    }

    //@RequestMapping(value = "/orders/full/{")

}
