package com.laingard.FrelloManager.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Stock")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;

    @NotBlank
    @Size(max = 30)
    private String name;
    private Double quantity;
    private Integer price;

    public Product() {
    }

    public Product(String name, Double quantity, Integer price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
