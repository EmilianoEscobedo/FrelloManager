package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.OrderDto;
import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.model.Order;
import com.laingard.FrelloManager.model.Product;

import java.util.List;

public interface OrderService {
    Order save(OrderDto order);
    List<OrderDto> findAll();
    OrderDto findOne(Long id);
    void deleteOne(Long id);
    Order update(OrderDto request, Long id, String attribute);
    void toDelivery(Long id);
    void toSalesBook(Long id);
    void delivered(Long id);
}
