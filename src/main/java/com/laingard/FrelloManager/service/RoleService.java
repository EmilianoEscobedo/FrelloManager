package com.laingard.FrelloManager.service;

import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.model.User;

public interface RoleService {
    User changeRole(RoleDto request);
}
