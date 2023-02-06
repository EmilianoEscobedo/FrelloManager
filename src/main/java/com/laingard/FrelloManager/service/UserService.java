package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;

import java.util.List;

public interface UserService {
    User save(UserDto user);
    List<UserDto> findAll();
    UserDto findOne(Long id);
    void deleteOne(Long id);
    User updateRole(RoleDto request, Long id);
}
