package com.laingard.FrelloManager.service.Impl;

import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.dto.SignUpDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.exception.AlreadyExistsException;
import com.laingard.FrelloManager.exception.CantBeEmptyException;
import com.laingard.FrelloManager.enumeration.ERole;
import com.laingard.FrelloManager.exception.ForbbidenException;
import com.laingard.FrelloManager.exception.NotFoundException;
import com.laingard.FrelloManager.mapper.UserMapper;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.repository.RoleRepository;
import com.laingard.FrelloManager.repository.UserRepository;
import com.laingard.FrelloManager.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserMapper userMapper;

    @Transactional
    @Override
    public User save(SignUpDto user) {
        if (userRepository.existsByUsername(user.getUsername().toLowerCase())) {
            throw new AlreadyExistsException("Error: Username already exist");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            throw new CantBeEmptyException("Error: Username cant be empty");
        }
        User nUser = new User(user.getUsername().toLowerCase(),
                encoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        nUser.setRole(userRole);
        return userRepository.save(nUser);
    }
    @Override
    public User updateRole(RoleDto request, Long id) {
        User user = userRepository.findOneById(id).
                orElseThrow(()-> new NotFoundException("Error: User not found"));
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
                throw new NotFoundException("Error: Role not found");
            }
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public UserDto findOne(Long id) {
        User user = userRepository.findOneById(id).
                orElseThrow(()-> new NotFoundException("Error: User not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void deleteOne(Long id){
        User user = userRepository.findOneById(id).
                orElseThrow(()-> new NotFoundException("Error: User not found"));
        if (user
                .getRole()
                .getName()
                .name()
                .equals("ROLE_ADMIN")) {
            throw new ForbbidenException("Error: Cant delete admin user");
        }
        userRepository.deleteById(user.getId());
    }
}
