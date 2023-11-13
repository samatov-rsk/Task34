package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class UserControllerTest extends BaseWeb {

    @Test
    @DisplayName("when request /user then return user page")
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_USER")
    public void testShowUserPage() throws Exception {
        var roles = List.of(new Role(1, "USER"));
        var user = new User(1, "aaa", "sss", 24, "aaaa", "1000", roles);

        when(securityUserService.getUser(any())).thenReturn(user);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user"));

        assertEquals(user, securityUserService.getUser(user.getEmail()));
        verify(securityUserService).getUser(user.getEmail());
    }

    @Test
    @DisplayName("when request /user then return UserNotFoundException")
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_USER")
    public void testShowUserPageNotFound() throws Exception {
        when(securityUserService.getUser(any())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/user"))
                .andExpect(status().isNotFound());
    }

}
