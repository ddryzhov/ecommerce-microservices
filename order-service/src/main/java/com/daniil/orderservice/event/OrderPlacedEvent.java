package com.daniil.orderservice.event;

import java.util.List;

public record OrderPlacedEvent(
        String orderNumber,
        List<OrderItemEvent> items
) {}
