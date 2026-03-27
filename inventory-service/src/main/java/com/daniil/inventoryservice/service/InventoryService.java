package com.daniil.inventoryservice.service;

import com.daniil.inventoryservice.dto.InventoryRequest;
import com.daniil.inventoryservice.dto.InventoryResponse;
import com.daniil.inventoryservice.dto.StockCheckRequest;
import com.daniil.inventoryservice.dto.StockCheckResponse;
import java.util.List;

public interface InventoryService {
    InventoryResponse addItem(InventoryRequest request);

    InventoryResponse getItem(String skuCode);

    List<InventoryResponse> getAllItems();

    InventoryResponse updateQuantity(String skuCode, Integer quantity);

    List<StockCheckResponse> checkStock(List<StockCheckRequest> requests);
}
