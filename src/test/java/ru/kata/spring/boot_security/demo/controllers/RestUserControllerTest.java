package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.kata.spring.boot_security.demo.BaseIT;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RestUserControllerTest extends BaseIT {

    @BeforeEach
    public void setUser() {
        var roles = List.of(new Role(1, "ROLE_USER"));
        var user = new User(1, "Milatik", "Samatov", 30, "user@mail.ru", "100", roles);
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("when request /api/user then return user json")
    public void testShowUserPage() throws Exception {

        mockMvc.perform(get("/api/user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("user@mail.ru"));
    }

    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

}
