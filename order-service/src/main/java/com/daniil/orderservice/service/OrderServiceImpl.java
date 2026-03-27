package com.daniil.orderservice.service;

import com.daniil.orderservice.model.Order;
import com.daniil.orderservice.model.OrderItem;
import com.daniil.orderservice.model.OrderStatus;
import com.daniil.orderservice.dto.OrderRequest;
import com.daniil.orderservice.dto.OrderResponse;
import com.daniil.orderservice.exception.OrderNotFoundException;
import com.daniil.orderservice.mapper.OrderMapper;
import com.daniil.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .status(OrderStatus.PENDING)
                .build();

        request.items().forEach(itemRequest -> {
            OrderItem item = orderMapper.toEntity(itemRequest);
            order.addItem(item);
        });

        BigDecimal total = order.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);
        log.info("Created order: {}", saved.getOrderNumber());
        return orderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Cannot cancel order with status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.REJECTED);
        log.info("Cancelled order: {}", order.getOrderNumber());
        return orderMapper.toResponse(orderRepository.save(order));
    }
}
