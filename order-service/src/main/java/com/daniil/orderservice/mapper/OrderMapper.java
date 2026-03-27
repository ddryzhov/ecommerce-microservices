package com.daniil.orderservice.mapper;

import com.daniil.orderservice.dto.OrderItemRequest;
import com.daniil.orderservice.dto.OrderItemResponse;
import com.daniil.orderservice.dto.OrderResponse;
import com.daniil.orderservice.model.Order;
import com.daniil.orderservice.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);

    OrderItemResponse toItemResponse(OrderItem item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemRequest request);
}
