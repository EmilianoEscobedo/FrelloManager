package com.laingard.FrelloManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laingard.FrelloManager.dto.SignUpDto;
import com.laingard.FrelloManager.dto.UserDto;
import com.laingard.FrelloManager.exception.NotFoundException;
import com.laingard.FrelloManager.security.jwt.TokenUtils;
import com.laingard.FrelloManager.security.services.UserDetailsServiceImpl;
import com.laingard.FrelloManager.service.Impl.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private TokenUtils tokenUtils;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private NotFoundException notFoundException;
    private SignUpDto signUpDto;
    private UserDto userDto;

    @BeforeEach
    public void init(){
        signUpDto = new SignUpDto();
        signUpDto.setUsername("testUserName");
        signUpDto.setPassword("testPassword");
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername(signUpDto.getUsername());
        userDto.setRole("USER");

    }

    @Test
    public void UserController_RegisterUser_ReturnCreated() throws Exception {

        given(userService.save(any(SignUpDto.class))).willReturn(userDto);
        // when
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDto)));
        // then
        response.andExpect(status().isOk());

        UserDto responseUser = objectMapper.readValue(response.andReturn().getResponse().getContentAsString(), UserDto.class);

        assertEquals(userDto.getId(), responseUser.getId());
    }

    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN" })
    public void UserController_UpdateRole_ReturnOk() throws Exception {
        // given
        Long userId = 1L;
        String newRole = "ADMIN";

        given(userService.updateRole(newRole, userId)).willReturn(userDto);

        // when
        ResultActions response = mockMvc.perform(put("/users/{id}", userId)
                .param("role", newRole));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.username", Matchers.is(userDto.getUsername())))
                .andExpect(jsonPath("$.role", Matchers.is(userDto.getRole())));

        verify(userService, times(1)).updateRole(newRole, userId);
    }

    @Test
    public void UserController_GetAll_ReturnListOfUsers() throws Exception {
        List<UserDto> userList = new ArrayList<>();
        userList.add(userDto);
        given(userService.findAll()).willReturn(userList);

        ResultActions response = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.is("testUserName")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role", Matchers.is("USER")));
    }

    @Test
    public void getOne_ReturnsUserDto_WhenUserExists() throws Exception {
        given(userService.findOne(userDto.getId())).willReturn(userDto);

        mockMvc.perform(get("/users/{id}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(userDto.getId().intValue())))
                .andExpect(jsonPath("$.username", Matchers.is(userDto.getUsername())))
                .andExpect(jsonPath("$.role", Matchers.is(userDto.getRole())));
    }

    @Test
    public void getOne_ReturnsNotFound_WhenUserDoesNotExist() throws Exception {
        given(userService.findOne(userDto.getId())).willThrow(new NotFoundException("User does not exist"));

        mockMvc.perform(get("/users/{id}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void UserController_DeleteOne_ReturnOk() throws Exception {
        Long id = 1L;
        doNothing().when(userService).deleteOne(id);

        mockMvc.perform(delete("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\":\"user ID" + id + " has been successfully deleted\"}"));
    }

}
