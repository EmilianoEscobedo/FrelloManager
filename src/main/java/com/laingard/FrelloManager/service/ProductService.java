package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.model.Product;

import java.util.List;

public interface ProductService {
    ProductDto save(ProductDto product);
    List<ProductDto> findAll();
    List<ProductDto> filterByAvailable();
    ProductDto findOne(Long id);
    void deleteOne(Long id);
    ProductDto update(ProductDto request, Long id, String attribute);
}
