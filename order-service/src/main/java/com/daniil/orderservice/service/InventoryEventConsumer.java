package com.daniil.orderservice.service;

import com.daniil.orderservice.event.InventoryReservationFailedEvent;
import com.daniil.orderservice.event.InventoryReservedEvent;
import com.daniil.orderservice.model.OrderStatus;
import com.daniil.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = "inventory-reserved", groupId = "order-service-group")
    @Transactional
    public void handleInventoryReserved(InventoryReservedEvent event) {
        log.info("Received InventoryReservedEvent for order: {}", event.orderNumber());

        orderRepository.findByOrderNumber(event.orderNumber())
                .ifPresentOrElse(
                        order -> {
                            order.setStatus(OrderStatus.APPROVED);
                            orderRepository.save(order);
                            log.info("Order {} APPROVED", event.orderNumber());
                        },
                        () -> log.warn("Order not found for number: {}", event.orderNumber())
                );
    }

    @KafkaListener(topics = "inventory-reservation-failed", groupId = "order-service-group")
    @Transactional
    public void handleInventoryReservationFailed(InventoryReservationFailedEvent event) {
        log.warn("Received InventoryReservationFailedEvent for order {}: {}",
                event.orderNumber(), event.reason());

        orderRepository.findByOrderNumber(event.orderNumber())
                .ifPresentOrElse(
                        order -> {
                            order.setStatus(OrderStatus.REJECTED);
                            orderRepository.save(order);
                            log.info("Order {} REJECTED. Reason: {}", event.orderNumber(), event.reason());
                        },
                        () -> log.warn("Order not found for number: {}", event.orderNumber())
                );
    }
}
