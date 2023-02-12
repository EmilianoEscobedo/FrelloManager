package com.laingard.FrelloManager.service.Impl;

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
import java.util.Map;

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
    public UserDto save(SignUpDto user) {
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

        userRepository.save(nUser);
        return userMapper.toDto(nUser);
    }
    @Override
    public UserDto updateRole(String RequestRole, Long id) {
        User user = userRepository.findOneById(id).
                orElseThrow(()-> new NotFoundException("Error: User not found"));
        Map<String, ERole> roleMap = Map.of(
        "user", ERole.ROLE_USER,
        "sales", ERole.ROLE_SALES,
        "kitchen", ERole.ROLE_KITCHEN,
        "delivery", ERole.ROLE_DELIVERY);
        ERole roleName = roleMap.get(RequestRole);
        if (roleName == null) {
            throw new NotFoundException("Error: Role not found");
        }
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        user.setRole(role);

        userRepository.save(user);
        return userMapper.toDto(user);
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
