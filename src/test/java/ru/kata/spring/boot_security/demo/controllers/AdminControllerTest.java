package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminControllerTest extends BaseWeb {

    @Test
    @DisplayName("when apply request /admin then return admin page")
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_ADMIN")
    public void testShowUserPage() throws Exception {
        var roles = List.of(new Role(1, "ROLE_ADMIN"));
        var user = new User(1, "test", "test", 24, "test", "1000", roles);

        when(securityUserService.getUser(anyString())).thenReturn(user);

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));

        verify(securityUserService).getUser(user.getEmail());
    }

    @Test
    @DisplayName("when apply request /admin then UserNotFoundException")
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_ADMIN")
    public void testShowAdminPageNotFound() throws Exception {
        when(securityUserService.getUser(anyString())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isNotFound());

        verify(securityUserService).getUser("test");
    }

}
