package com.daniil.inventoryservice.event;

public record InventoryReservedEvent(
        String orderNumber
) {}
