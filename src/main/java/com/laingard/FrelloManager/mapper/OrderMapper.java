package com.laingard.FrelloManager.mapper;

import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.dto.OrderProductDto;
import com.laingard.FrelloManager.enumeration.EState;
import com.laingard.FrelloManager.exception.NotFoundException;
import com.laingard.FrelloManager.model.Order;
import com.laingard.FrelloManager.model.State;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "products", expression = "java(mapProducts(order))")
    @Mapping(target = "timeStamp", qualifiedByName = "formatDate")
    @Mapping(target = "state", expression = "java(order.getState().getName().name())")
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
            dto.setName(product.getProduct().getName());
            dto.setQuantity(product.getQuantity());
            return dto;
        }).collect(Collectors.toList());
    }

    default EState stateToEState(String state){
        Map<String, EState> stateMap = Map.of(
                "cooking", EState.COOKING,
                "delivery", EState.DELIVERY,
                "delivered", EState.DELIVERED,
                "payed", EState.PAYED,
                "canceled", EState.CANCELED
        );
        return stateMap.get(state);
    }
}
