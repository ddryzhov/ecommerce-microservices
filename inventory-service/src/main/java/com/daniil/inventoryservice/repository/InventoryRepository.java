package com.daniil.inventoryservice.repository;

import com.daniil.inventoryservice.model.InventoryItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findBySkuCode(String skuCode);

    List<InventoryItem> findBySkuCodeIn(List<String> skuCodes);

    boolean existsBySkuCode(String skuCode);
}
