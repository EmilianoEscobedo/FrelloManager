package com.laingard.FrelloManager.service.Impl;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.exception.UsernameAlreadyExistsException;
import com.laingard.FrelloManager.exception.UsernameCantBeEmptyException;
import com.laingard.FrelloManager.exception.UsernameNotFoundException;
import com.laingard.FrelloManager.enumeration.ERole;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.repository.RoleRepository;
import com.laingard.FrelloManager.repository.UserRepository;
import com.laingard.FrelloManager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "signupService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    public UserServiceImpl() {
        super();
    }

    @Override
    public User save(UserDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Error: Username already exist");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            throw new UsernameCantBeEmptyException("Error: Username cant be empty");
        }
        User nUser = new User(user.getUsername(),
                encoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
        nUser.setRole(userRole);
        return userRepository.save(nUser);
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public User findOne(String username) {
        if (userRepository.findOneByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("Error: Username not found");
        }
        return userRepository.findOneByUsername(username).get();
    }

    @Override
    public void deleteOne(String username){
        if (userRepository.findOneByUsername(username).isEmpty()) {
            throw new UsernameNotFoundException("Error: Username not found");
        }
        User user = userRepository.findOneByUsername(username).get();
        userRepository.deleteById(user.getId());
    }
}
