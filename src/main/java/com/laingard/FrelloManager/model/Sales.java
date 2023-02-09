package com.laingard.FrelloManager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "SalesBook")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private final LocalDate timeStamp = LocalDate.now();

    public Sales() {
    }

    public Sales(Long id, Double amount) {
        this.id = id;
        this.amount = amount;
    }
}
