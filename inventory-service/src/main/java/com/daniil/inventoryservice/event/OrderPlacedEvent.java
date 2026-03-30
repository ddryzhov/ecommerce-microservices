package com.daniil.inventoryservice.event;

import java.util.List;

public record OrderPlacedEvent(
        String orderNumber,
        List<OrderItemEvent> items
) {}
