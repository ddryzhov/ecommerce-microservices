package com.daniil.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockCheckRequest(
        @NotBlank String skuCode,
        @NotNull @Positive Integer requiredQuantity
) {}
