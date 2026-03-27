package com.daniil.inventoryservice.dto;

public record InventoryResponse(
        Long id,
        String skuCode,
        Integer quantity,
        boolean inStock
) {}
