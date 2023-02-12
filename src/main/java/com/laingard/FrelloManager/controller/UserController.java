package com.laingard.FrelloManager.controller;

import com.laingard.FrelloManager.dto.MessageResponse;
import com.laingard.FrelloManager.dto.SignUpDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Error: Username cant be empty",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Error: Username already exist",
                    content = @Content)})
    @PostMapping()
    public UserDto registerUser(@io.swagger.v3.oas.annotations.parameters.RequestBody
                                            (description = "Username/Pass of the new user")
                                    @Valid @RequestBody SignUpDto signUpRequest) {
        return userService.save(signUpRequest);
    }

    @Operation(summary = "Update user role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role assigned successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: User not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: Role not found",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public UserDto updateRole(@Parameter(description = "Id of the user to be updated") @PathVariable Long id,
                              @Parameter(description = "New role of the user")@RequestParam String role){
        return userService.updateRole(role, id);
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public List<UserDto> getAll(){
        return userService.findAll();
    }

    @Operation(summary = "Get one user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One user",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: User not found",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("{id}")
    public UserDto getOne(@Parameter(description = "Id of the user to get")
                              @PathVariable("id") Long id){
        return userService.findOne(id);
    }

    @Operation(summary = "Delete one user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user ID {id} has been successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Error: Authorization Required",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Error: Access Denied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error: User not found",
                    content = @Content)})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOne (@Parameter(description = "Id of the user to be deleted")
                                            @PathVariable("id") Long id){
        userService.deleteOne(id);
        return ResponseEntity.ok(new MessageResponse("user ID" + id + " has been successfully deleted"));
    }
}
