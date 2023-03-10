package com.laingard.FrelloManager.repository;

import com.laingard.FrelloManager.enumeration.ERole;
import com.laingard.FrelloManager.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
