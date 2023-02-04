package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;

import java.util.List;

public interface UserService {
    User save(UserDto user);
    List<User> findAll();
    User findOne(String username);
    void deleteOne(String username);
}
