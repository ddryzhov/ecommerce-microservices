package com.daniil.inventoryservice.service;

import com.daniil.inventoryservice.dto.InventoryRequest;
import com.daniil.inventoryservice.dto.InventoryResponse;
import com.daniil.inventoryservice.dto.StockCheckRequest;
import com.daniil.inventoryservice.dto.StockCheckResponse;
import com.daniil.inventoryservice.model.InventoryItem;
import com.daniil.inventoryservice.exception.InventoryItemNotFoundException;
import com.daniil.inventoryservice.mapper.InventoryMapper;
import com.daniil.inventoryservice.repository.InventoryRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    @Transactional
    public InventoryResponse addItem(InventoryRequest request) {
        if (inventoryRepository.existsBySkuCode(request.skuCode())) {
            throw new IllegalArgumentException("SKU already exists: " + request.skuCode());
        }
        InventoryItem item = inventoryMapper.toEntity(request);
        InventoryItem saved = inventoryRepository.save(item);
        log.info("Added inventory item: {}", saved.getSkuCode());
        return inventoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getItem(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode)
                .map(inventoryMapper::toResponse)
                .orElseThrow(() -> new InventoryItemNotFoundException(skuCode));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllItems() {
        return inventoryMapper.toResponseList(inventoryRepository.findAll());
    }

    @Override
    @Transactional
    public InventoryResponse updateQuantity(String skuCode, Integer quantity) {
        InventoryItem item = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new InventoryItemNotFoundException(skuCode));
        item.setQuantity(quantity);
        log.info("Updated quantity for SKU {}: {}", skuCode, quantity);
        return inventoryMapper.toResponse(inventoryRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockCheckResponse> checkStock(List<StockCheckRequest> requests) {
        List<String> skuCodes = requests.stream()
                .map(StockCheckRequest::skuCode)
                .toList();

        Map<String, InventoryItem> inventory = inventoryRepository
                .findBySkuCodeIn(skuCodes).stream()
                .collect(Collectors.toMap(InventoryItem::getSkuCode, Function.identity()));

        return requests.stream()
                .map(req -> {
                    InventoryItem item = inventory.get(req.skuCode());
                    int available = item != null ? item.getQuantity() : 0;
                    return new StockCheckResponse(
                            req.skuCode(),
                            available >= req.requiredQuantity(),
                            available
                    );
                })
                .toList();
    }
}
