package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;
import com.laingard.FrelloManager.service.RoleService;
import com.laingard.FrelloManager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto signUpRequest) {
        userService.save(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User successfully registered"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/changerole")
    public ResponseEntity<MessageResponse> changeRole (@RequestBody RoleDto request){
        roleService.changeRole(request);
        return ResponseEntity.ok(new MessageResponse("Role assigned successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<User> getAll(){
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{name}")
    public User getOne(@PathVariable("name") String name){
        return userService.findOne(name);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{name}")
    public ResponseEntity<MessageResponse> deleteOne (@PathVariable("name") String name){
        userService.deleteOne(name);
        return ResponseEntity.ok(new MessageResponse("user " + name + " has been successfully deleted"));
    }

}
