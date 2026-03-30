package com.daniil.inventoryservice.service;

import com.daniil.inventoryservice.event.InventoryReservationFailedEvent;
import com.daniil.inventoryservice.event.InventoryReservedEvent;
import com.daniil.inventoryservice.event.OrderPlacedEvent;
import com.daniil.inventoryservice.model.ProcessedEvent;
import com.daniil.inventoryservice.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {

    private final InventoryService inventoryService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = "order-placed", groupId = "inventory-service-group")
    @Transactional
    public void handleOrderPlaced(OrderPlacedEvent event) {
        log.info("Received OrderPlacedEvent for order: {}", event.orderNumber());

        ProcessedEvent.ProcessedEventId eventId =
                new ProcessedEvent.ProcessedEventId(event.orderNumber(), "OrderPlacedEvent");

        if (processedEventRepository.existsById(eventId)) {
            log.warn("Duplicate event detected for order: {}, skipping", event.orderNumber());
            return;
        }

        try {
            inventoryService.reserveStock(event.orderNumber(), event.items());

            processedEventRepository.save(
                    ProcessedEvent.builder()
                            .id(eventId)
                            .processedAt(java.time.LocalDateTime.now())
                            .build()
            );

            kafkaTemplate.send(
                    "inventory-reserved",
                    event.orderNumber(),
                    new InventoryReservedEvent(event.orderNumber())
            );
            log.info("Stock reserved for order: {}", event.orderNumber());

        } catch (Exception ex) {
            log.warn("Stock reservation failed for order {}: {}",
                    event.orderNumber(), ex.getMessage());

            kafkaTemplate.send(
                    "inventory-reservation-failed",
                    event.orderNumber(),
                    new InventoryReservationFailedEvent(event.orderNumber(), ex.getMessage())
            );
        }
    }
}
