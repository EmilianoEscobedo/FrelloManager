package com.laingard.FrelloManager.mapper;

import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.dto.OrderProductDto;
import com.laingard.FrelloManager.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "products", expression = "java(mapProducts(order))")
    @Mapping(target = "timeStamp", qualifiedByName = "formatDate")
    @Mapping(target = "state", expression = "java(order.getState().getName())")
    OrderDto toDto(Order order);

    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "state", ignore = true)
    Order toEntity(OrderDto dto);

    @Named("formatDate")
    default String formatDate(String isoDate) {
        LocalDateTime date = LocalDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME);
        return date.format(DateTimeFormatter.ofPattern("dd-MM HH:mm"));
    }

    default List<OrderDto> toDtoList(List<Order> OrderList){
        if (OrderList == null)
            return new ArrayList<>();
        return OrderList.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<OrderProductDto> mapProducts(Order order) {
        return order.getProducts().stream().map(product -> {
            OrderProductDto dto = new OrderProductDto();
            dto.setId(product.getProduct().getId());
            dto.setQuantity(product.getQuantity());
            return dto;
        }).collect(Collectors.toList());
    }

}
