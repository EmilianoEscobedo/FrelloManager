package com.laingard.FrelloManager.mapper;

import com.laingard.FrelloManager.dto.ProductDto;
import com.laingard.FrelloManager.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

    default List<ProductDto> toDtoList(List<Product> productList){
        if (productList == null)
            return new ArrayList<>();
        return productList.stream().map(this::toDto).collect(Collectors.toList());
    }
}
