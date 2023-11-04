package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestUserControllerTest extends BaseWeb {


    @Test
    @DisplayName("when request /api/user then return user json")
    @WithMockUser(username = "test", password = "test", authorities = "ROLE_USER")
    public void testShowUserPage() throws Exception {
        var roles = List.of(new Role(1, "USER"));
        var user = new User(1, "aaa", "sss", 24, "test", "test", roles);

        when(securityUserService.getUser(user.getEmail())).thenReturn(user);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test"))
                .andExpect(jsonPath("$.roles[0].name").value("USER"));


//        assertEquals(user, response.getBody());
//        verify(securityUserService).getUser(user.getEmail());
    }

}
