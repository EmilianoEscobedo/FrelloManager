package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.FilteredOrderDto;
import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.model.Order;

import java.util.List;

public interface OrderService {
    OrderDto save(OrderDto order);
    List<OrderDto> findAll();
    FilteredOrderDto findByDate(String state, String from, String to);
    OrderDto findOne(Long id);
    void deleteOne(Long id);
    OrderDto update(OrderDto request, Long id, String attribute);
}
