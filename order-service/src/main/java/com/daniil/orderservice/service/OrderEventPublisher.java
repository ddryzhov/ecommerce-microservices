package com.daniil.orderservice.service;

import com.daniil.orderservice.event.OrderItemEvent;
import com.daniil.orderservice.event.OrderPlacedEvent;
import com.daniil.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderPlaced(Order order) {
        OrderPlacedEvent event = new OrderPlacedEvent(
                order.getOrderNumber(),
                order.getItems().stream()
                        .map(item -> new OrderItemEvent(
                                item.getSkuCode(),
                                item.getQuantity(),
                                item.getPrice()))
                        .toList()
        );

        kafkaTemplate.send("order-placed", order.getOrderNumber(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish OrderPlacedEvent for order {}: {}",
                                order.getOrderNumber(), ex.getMessage());
                    } else {
                        log.info("Published OrderPlacedEvent for order {} to partition {}",
                                order.getOrderNumber(),
                                result.getRecordMetadata().partition());
                    }
                });
    }
}
