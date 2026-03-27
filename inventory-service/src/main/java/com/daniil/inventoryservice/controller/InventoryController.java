package com.daniil.inventoryservice.controller;

import com.daniil.inventoryservice.dto.InventoryRequest;
import com.daniil.inventoryservice.dto.InventoryResponse;
import com.daniil.inventoryservice.dto.StockCheckRequest;
import com.daniil.inventoryservice.dto.StockCheckResponse;
import com.daniil.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse addItem(@Valid @RequestBody InventoryRequest request) {
        return inventoryService.addItem(request);
    }

    @GetMapping
    public List<InventoryResponse> getAllItems() {
        return inventoryService.getAllItems();
    }

    @GetMapping("/{skuCode}")
    public InventoryResponse getItem(@PathVariable String skuCode) {
        return inventoryService.getItem(skuCode);
    }

    @PutMapping("/{skuCode}/quantity")
    public InventoryResponse updateQuantity(@PathVariable String skuCode,
                                            @RequestParam Integer quantity) {
        return inventoryService.updateQuantity(skuCode, quantity);
    }

    @PostMapping("/check")
    public List<StockCheckResponse> checkStock(@Valid @RequestBody List<@Valid StockCheckRequest> requests) {
        return inventoryService.checkStock(requests);
    }
}
