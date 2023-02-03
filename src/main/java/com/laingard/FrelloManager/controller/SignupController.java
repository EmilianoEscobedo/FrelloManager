package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.services.SignupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/auth")
public class SignupController {
    @Autowired
    SignupService signupService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto signUpRequest) {
        signupService.save(signUpRequest);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
