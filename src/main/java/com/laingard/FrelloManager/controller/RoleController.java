package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.services.RoleService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    @Autowired
    RoleService roleService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<MessageResponse> assign (@Valid @RequestBody UserDto request){
        roleService.assignRole(request);
        return ResponseEntity.ok(new MessageResponse("Role assigned successfully"));
    }

}
