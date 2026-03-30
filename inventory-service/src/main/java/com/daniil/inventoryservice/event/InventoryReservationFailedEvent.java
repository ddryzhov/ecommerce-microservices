package com.daniil.inventoryservice.event;

public record InventoryReservationFailedEvent(
        String orderNumber,
        String reason
) {}
