package com.laingard.FrelloManager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private Double quantity;
    private Double price;
}
