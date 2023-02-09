package com.laingard.FrelloManager.dto;

import com.laingard.FrelloManager.enumeration.EState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private List<OrderProductDto> products;
    private String timeStamp;
    private Double totalPrice;
    private EState state;
    private String clientName;
    private String clientPhone;
    private String clientAddress;
}
