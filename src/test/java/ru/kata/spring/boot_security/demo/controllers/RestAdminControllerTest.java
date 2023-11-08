package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestAdminControllerTest extends BaseWeb {

    @Test
    @DisplayName("when request /api/users for get all users then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetAllUser() throws Exception {

        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20, "user@mail.ru", "test", roles);
        var user2 = new User(2, "user2", "adminov", 20, "admin@mail.ru", "test", roles);

        var users = List.of(user, user2);

        when(userService.getAllUsers()).thenReturn(users);

        String json = objectMapper.writeValueAsString(users);

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));
    }

    @Test
    @DisplayName("when request /api/user/1 for get user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetUser() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20, "user@mail.ru", "test", roles);

        when(userService.getUserById(user.getId())).thenReturn(user);
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));
    }

    @Test
    @DisplayName("when request /api/users for add user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testAddUser() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20, "user@mail.ru", "test", roles);

        when(userService.addUser(user)).thenReturn(any());

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(
                        json))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("when request /api/users/1 for update user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testUpdateUserById() throws Exception {

        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "admin", "admin", 20, "admin@mail.ru", "test", roles);

        when(userService.updateUserById(1,user)).thenReturn(user);

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    @DisplayName("when request /api/users/1 for delete user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testDeleteUserById() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20, "user@mail.ru", "test", roles);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(content().json(json, false));
    }

    @Test
    @DisplayName("when request /api/users/about-user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testShowUserPage() throws Exception {

        var roles = List.of(new Role(1, "ROLE_ADMIN"));
        var user = new User(1, "admin", "adminov", 20, "admin@mail.ru", "test", roles);

        when(securityUserService.getCurrentUser()).thenReturn(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/api/users/about-user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));
    }
}
