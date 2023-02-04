package com.laingard.FrelloManager.model;

import com.laingard.FrelloManager.enumeration.EState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    @NotBlank
    @Size(max = 30)
    private List<Integer> products;
    private final LocalDate timeStamp = LocalDate.now();
    @NotBlank
    private Double totalPrice;
    @NotBlank
    private EState state;
    @NotBlank
    private String clientName;
    private Integer clientPhone;
    @NotBlank
    private String clientAddress;






}
