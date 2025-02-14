package com.obito.Order_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obito.Order_service.dto.OrderResponseDto;
import com.obito.Order_service.dto.PaymentDto;
import com.obito.Order_service.dto.UserDto;
import com.obito.Order_service.entity.Order;
import com.obito.Order_service.repository.OrderRepository;
import lombok.AccessLevel;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    RestTemplate restTemplate;

    public String placeOrder(@RequestBody Order  order) throws JsonProcessingException {
        //save in DB
        order.setPurchaseDate(new Date());
        order.setOrderId(UUID.randomUUID().toString().split("-")[0]);
        orderRepository.save(order);
        //send to payment service through kafka
        kafkaTemplate.send(topicName,new ObjectMapper().writeValueAsString(order));
        return "Order placed Succefully with OrderId:"+order.getOrderId()+"..we will notify about your Confirmation....";
    }

    public OrderResponseDto getOrder(String orderId){
        //order Details from own DB
        orderRepository.findById(5);
        Order order=orderRepository.findByOrderId(orderId);
        //Payment details from rest call from payment service
        PaymentDto paymentDto=restTemplate.getForObject("http://PAYMENT-SERVICE/payment/get/"+orderId, PaymentDto.class);

        //User details from rest call from user service
        UserDto userDto=restTemplate.getForObject("http://USER-SERVICE/user/get?id="+order.getUserId(), UserDto.class);
        OrderResponseDto orderResponseDto=new OrderResponseDto();
        orderResponseDto.setOrder(order);
        orderResponseDto.setPaymentDto(paymentDto);
        orderResponseDto.setUserDto(userDto);
        return orderResponseDto;
    }

}
