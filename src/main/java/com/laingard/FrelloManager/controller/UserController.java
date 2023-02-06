package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.RoleDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.model.User;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto signUpRequest) {
        userService.save(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User successfully registered"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updaterole/{id}")
    public ResponseEntity<?> changeRole (@RequestBody RoleDto request,
                                         @PathVariable Long id){
        userService.updateRole(request, id);
        return ResponseEntity.ok(new MessageResponse("Role assigned successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<UserDto> getAll(){
        return userService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public UserDto getOne(@PathVariable("id") Long id){
        return userService.findOne(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne (@PathVariable("id") Long id){
        userService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("user ID" + id + " has been successfully deleted"));
    }
}
