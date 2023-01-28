package com.laingard.FrelloManager.repositories;

import com.laingard.FrelloManager.models.ERole;
import com.laingard.FrelloManager.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<ERole, Long> {
    Optional<Role> findByName(ERole name);
}
