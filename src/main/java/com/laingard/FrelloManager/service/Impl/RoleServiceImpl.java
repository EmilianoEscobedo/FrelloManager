package com.laingard.FrelloManager.service.Impl;

import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.exception.RoleNotFoundException;
import com.laingard.FrelloManager.exception.UsernameNotFoundException;
import com.laingard.FrelloManager.enumeration.ERole;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.repository.RoleRepository;
import com.laingard.FrelloManager.repository.UserRepository;
import com.laingard.FrelloManager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User changeRole(RoleDto request) {
        if (userRepository.findOneByUsername(request.getUsername()).isEmpty()) {
            throw new UsernameNotFoundException("Error: Username not found");
        }
        User user = userRepository.findOneByUsername(request.getUsername()).get();
        switch (request.getRole()) {
            case "user" -> {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                user.setRole(userRole);
            }
            case "sales" -> {
                Role salesRole = roleRepository.findByName(ERole.ROLE_SALES)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                user.setRole(salesRole);
            }
            case "kitchen" -> {
                Role kitchenRole = roleRepository.findByName(ERole.ROLE_KITCHEN)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                user.setRole(kitchenRole);
            }
            case "delivery" -> {
                Role deliveryRole = roleRepository.findByName(ERole.ROLE_DELIVERY)
                        .orElseThrow(() -> new RuntimeException("Error: Role not found."));
                user.setRole(deliveryRole);
            }
            default -> {
                throw new RoleNotFoundException("Error: Role not found");
            }
        }
        return userRepository.save(user);
    }
}
