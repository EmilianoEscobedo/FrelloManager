package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.SignUpDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;

import java.util.List;

public interface UserService {
    UserDto save(SignUpDto user);
    List<UserDto> findAll();
    UserDto findOne(Long id);
    void deleteOne(Long id);
    UserDto updateRole(String role, Long id);
}
