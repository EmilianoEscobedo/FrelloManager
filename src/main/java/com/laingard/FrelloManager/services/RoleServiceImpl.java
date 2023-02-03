package com.laingard.FrelloManager.services;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.exception.RoleNotFoundException;
import com.laingard.FrelloManager.exception.UsernameNotFoundException;
import com.laingard.FrelloManager.model.ERole;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.repository.RoleRepository;
import com.laingard.FrelloManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User assignRole(UserDto request) {
        if (userRepository.findOneByUsername(request.getUsername()).isEmpty()) {
            throw new UsernameNotFoundException("Error: Username not found");
        }
        User user = userRepository.findOneByUsername(request.getUsername()).get();
        switch (request.getRole()) {
            case "user" -> {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                user.setRole(userRole);
            }
            case "sales" -> {
                Role salesRole = roleRepository.findByName(ERole.ROLE_SALES)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                user.setRole(salesRole);
            }
            case "kitchen" -> {
                Role kitchenRole = roleRepository.findByName(ERole.ROLE_KITCHEN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                user.setRole(kitchenRole);
            }
            case "delivery" -> {
                Role deliveryRole = roleRepository.findByName(ERole.ROLE_DELIVERY)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                user.setRole(deliveryRole);
            }
            default -> {
                throw new RoleNotFoundException("Error: Role is not found");
            }
        }
        return userRepository.save(user);
    }
}
