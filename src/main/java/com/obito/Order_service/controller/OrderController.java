package com.obito.Order_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.obito.Order_service.entity.Order;
import com.obito.Order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    //validation
    //logging
    //exceptionHandling
    @PostMapping("/place")
    public String placeOrder(@RequestBody Order order) throws JsonProcessingException {
        return orderService.placeOrder(order);
    }
    @GetMapping("/{orderId}")
    public String get(@PathVariable("orderId") String orderId){
        return orderService.getOrder(orderId);
    }
}
