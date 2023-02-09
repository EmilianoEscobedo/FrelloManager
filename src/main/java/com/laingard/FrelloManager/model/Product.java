package com.laingard.FrelloManager.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "Stock")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String name;
    private Double quantity;
    private Double price;

    public Product() {
    }

    public Product(String name, Double quantity, Double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
