package com.laingard.FrelloManager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilteredOrderDto {
    private List<OrderDto> orders;
    private Integer totalOrders;
    private Double totalMoney;
}
