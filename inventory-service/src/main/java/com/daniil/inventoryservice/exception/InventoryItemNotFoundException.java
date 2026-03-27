package com.daniil.inventoryservice.exception;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(String skuCode) {
        super("Inventory item not found for SKU: " + skuCode);
    }
}
