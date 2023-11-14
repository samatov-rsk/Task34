package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.NonUniqueResultException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestAdminControllerTest extends BaseWeb {

    @Test
    @DisplayName("when apply request /api/users then return all users json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetAllUser() throws Exception {

        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20,
                "user@mail.ru", "test", roles);
        var user2 = new User(2, "user2", "adminov", 20,
                "admin@mail.ru", "test", roles);

        var users = List.of(user, user2);

        when(userService.getAllUsers()).thenReturn(users);

        String json = objectMapper.writeValueAsString(users);

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("when apply request /api/users then return empty list")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetAllUserNotFound() throws Exception {

        var users = List.of();

        when(userService.getAllUsers()).thenReturn(List.of());

        String json = objectMapper.writeValueAsString(users);

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));

        verify(userService).getAllUsers();
    }

    @Test
    @DisplayName("when apply request /api/users/{userId} then return user with {userId} json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetUser() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20,
                "user@mail.ru", "test", roles);

        when(userService.getUserById(user.getId())).thenReturn(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));

        verify(userService).getUserById(user.getId());
    }

    @Test
    @DisplayName("when apply request /api/users/99 then return UserNotFoundException")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testGetUserNotFound() throws Exception {

        when(userService.getUserById(99)).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(99);
    }

    @Test
    @DisplayName("when apply request /api/users for add user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testAddUser() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20,
                "user@mail.ru", "test", roles);

        when(userService.addUser(user)).thenReturn(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(
                        json))
                .andExpect(status().isOk());

        verify(userService).addUser(user);
    }

    @Test
    @DisplayName("when apply request /api/users for add user with non-unique email then return NonUniqueException")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testAddUserNonUniqueEmail() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20,
                "existing_user@mail.ru", "test", roles);

        when(userService.addUser(user)).thenThrow(new NonUniqueResultException("Email not unique"));

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when request /api/users/1 for update user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testUpdateUserById() throws Exception {

        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "admin", "admin", 20,
                "admin@mail.ru", "test", roles);

        when(userService.updateUserById(any(), any())).thenReturn(user);

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().json(userJson));
    }

    @Test
    @DisplayName("when request /api/users/1 for get user then return exception UserNotFoundException")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testUpdateUserNotFound() throws Exception {

        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "admin", "admin", 20,
                "admin@mail.ru", "test", roles);

        when(userService.updateUserById(any(), any())).thenThrow(new UserNotFoundException("User not found"));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("when request /api/users/1 for delete user then return user json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testDeleteUserById() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20,
                "user@mail.ru", "test", roles);

        doNothing().when(userService).removeUser(user.getId());

        String json = objectMapper.writeValueAsString(user);


        mockMvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("when request /api/users/1 for delete user then return UserNotFoundException")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testDeleteUserByIdNotFound() throws Exception {

        doThrow(new UserNotFoundException("User not found")).when(userService).removeUser(99);

        mockMvc.perform(delete("/api/users/99").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("when request /api/users/about-user then return admin json")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testShowAdminPage() throws Exception {

        var roles = List.of(new Role(1, "ROLE_ADMIN"));
        var user = new User(1, "admin", "adminov", 20,
                "admin@mail.ru", "test", roles);

        when(securityUserService.getCurrentUser()).thenReturn(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/api/users/about-user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(json, false));
    }

    @Test
    @DisplayName("when request /api/users/about-user then return UserNotFoundException")
    @WithMockUser(username = "admin@mail.ru", password = "test", authorities = "ROLE_ADMIN")
    public void testShowAdminPageNotFound() throws Exception {

        when(securityUserService.getCurrentUser()).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/about-user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
