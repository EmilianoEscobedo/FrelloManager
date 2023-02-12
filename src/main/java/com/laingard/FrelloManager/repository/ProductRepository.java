package com.laingard.FrelloManager.repository;

import com.laingard.FrelloManager.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    Boolean existsByName(String name);
    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> filterByAvailable();
}
