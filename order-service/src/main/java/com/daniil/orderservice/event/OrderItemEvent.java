package com.daniil.orderservice.event;

import java.math.BigDecimal;

public record OrderItemEvent(
        String skuCode,
        Integer quantity,
        BigDecimal price
) {}
