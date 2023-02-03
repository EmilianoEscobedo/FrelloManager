package com.laingard.FrelloManager.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "SalesBook")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private final LocalDate timeStamp = LocalDate.now();


}
