package com.daniil.inventoryservice.dto;

public record StockCheckResponse(
        String skuCode,
        boolean sufficient,
        int available
) {}
