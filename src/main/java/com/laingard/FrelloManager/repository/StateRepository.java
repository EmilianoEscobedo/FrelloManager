package com.laingard.FrelloManager.repository;

import com.laingard.FrelloManager.enumeration.EState;
import com.laingard.FrelloManager.model.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findByName(EState name);
}
