package com.daniil.orderservice.repository;

import com.daniil.orderservice.model.Order;
import com.daniil.orderservice.model.OrderStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByStatus(OrderStatus status);
}
