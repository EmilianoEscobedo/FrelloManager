package com.laingard.FrelloManager.services;

import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;

public interface SignupService {
    User save(UserDto user);
}
