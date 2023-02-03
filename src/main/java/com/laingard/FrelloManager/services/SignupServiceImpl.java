package com.laingard.FrelloManager.services;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.exception.UsernameAlreadyExistsException;
import com.laingard.FrelloManager.exception.UsernameCantBeEmptyException;
import com.laingard.FrelloManager.model.ERole;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.repository.RoleRepository;
import com.laingard.FrelloManager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service(value = "signupService")
public class SignupServiceImpl implements SignupService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public User save(UserDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Error: Username already exist");
        }
        if (user.getUsername() == null || user.getUsername().equals("")) {
            throw new UsernameCantBeEmptyException("Error: Username cant be empty");
        }
        // Create new user's account
        User nUser = new User(user.getUsername(),
                encoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        nUser.setRole(userRole);
        return userRepository.save(nUser);
    }
}
