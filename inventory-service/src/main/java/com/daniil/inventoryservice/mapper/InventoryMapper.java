package com.daniil.inventoryservice.mapper;

import com.daniil.inventoryservice.model.InventoryItem;
import com.daniil.inventoryservice.dto.InventoryRequest;
import com.daniil.inventoryservice.dto.InventoryResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "inStock", expression = "java(item.getQuantity() > 0)")
    InventoryResponse toResponse(InventoryItem item);

    List<InventoryResponse> toResponseList(List<InventoryItem> items);

    @Mapping(target = "id", ignore = true)
    InventoryItem toEntity(InventoryRequest request);
}
