package com.daniil.orderservice.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String skuCode,
        Integer quantity,
        BigDecimal price
) {}
