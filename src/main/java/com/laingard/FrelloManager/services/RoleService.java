package com.laingard.FrelloManager.services;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.Role;
import com.laingard.FrelloManager.model.User;

import java.util.List;

public interface RoleService {
    User assignRole(UserDto request);
//    List<User> findAll();
//    User findOne(String username);
}
