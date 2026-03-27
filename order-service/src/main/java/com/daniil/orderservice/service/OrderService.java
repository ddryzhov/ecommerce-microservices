package com.daniil.orderservice.service;

import com.daniil.orderservice.dto.OrderRequest;
import com.daniil.orderservice.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest request);
    OrderResponse getOrder(Long id);
    List<OrderResponse> getAllOrders();
    OrderResponse cancelOrder(Long id);
}
