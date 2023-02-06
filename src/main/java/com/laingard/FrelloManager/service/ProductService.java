package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.model.Product;

import java.util.List;

public interface ProductService {
    Product save(ProductDto product);
    List<ProductDto> findAll();
    ProductDto findOne(Long id);
    void deleteOne(Long id);
    Product update(ProductDto request, Long id, String attribute);
}
