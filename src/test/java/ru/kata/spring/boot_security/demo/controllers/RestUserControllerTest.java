package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestUserControllerTest extends BaseWeb {

    @Test
    @DisplayName("when apply request /api/user then return user json")
    @WithMockUser(username = "user@mail.ru", password = "test", authorities = "ROLE_USER")
    public void testShowUserPage() throws Exception {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "user", "userov", 20, "user@mail.ru", "test", roles);

        when(securityUserService.getUser(user.getEmail())).thenReturn(user);

        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(json, false));

        verify(securityUserService).getUser(user.getEmail());
    }

    @Test
    @DisplayName("when apply request /api/user then return UserNotFoundException")
    @WithMockUser(username = "user@mail.ru", password = "test", authorities = "ROLE_USER")
    public void testShowUserPageNotFound() throws Exception {
        when(securityUserService.getUser("user@mail.ru")).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isNotFound());

        verify(securityUserService).getUser("user@mail.ru");
    }

}
