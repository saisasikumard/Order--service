package com.obito.Order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obito.Order_service.entity.Order;
import com.obito.Order_service.repository.OrderRepository;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Value("${my.producer.topic.name}")
    String topicName;

    @Autowired
    KafkaTemplate<String,Object> kafkaTemplate;

    public String placeOrder(@RequestBody Order  order) throws JsonProcessingException {
        //save in DB
        order.setPurchaseDate(new Date());
        order.setOrderId(UUID.randomUUID().toString().split("-")[0]);
        orderRepository.save(order);
        //send to payment service through kafka
        kafkaTemplate.send(topicName,new ObjectMapper().writeValueAsString(order));
        return "Order placed Succefully with OrderId:"+order.getOrderId()+"..we will notify about your Confirmation....";
    }

    public String getOrder(String orderId){
        return null;
    }

}
