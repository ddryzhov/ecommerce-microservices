package com.daniil.orderservice.event;

public record InventoryReservedEvent(
        String orderNumber
) {}
