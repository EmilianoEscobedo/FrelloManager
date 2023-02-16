package com.laingard.FrelloManager.repository;

import com.laingard.FrelloManager.model.Order;
import com.laingard.FrelloManager.model.Product;
import com.laingard.FrelloManager.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.timeStamp >= :startDate AND o.timeStamp <= :endDate AND o.state = :state")
    List<Order> filterByDateState(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("state") State state);

}
