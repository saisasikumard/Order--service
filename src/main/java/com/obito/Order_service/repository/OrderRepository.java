package com.obito.Order_service.repository;

import com.obito.Order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByOrderId(String orderId);
}
