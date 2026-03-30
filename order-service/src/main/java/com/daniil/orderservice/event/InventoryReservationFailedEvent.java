package com.daniil.orderservice.event;

public record InventoryReservationFailedEvent(
        String orderNumber,
        String reason
) {}
