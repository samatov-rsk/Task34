package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest({RestAdminController.class})
public class RestAdminControllerTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setUser() {
        var roles = List.of(new Role(1, "ROLE_ADMIN"));
        var user = new User(1, "Milatik", "Samatov", 30, "admin@mail.ru", "100", roles);
        userRepository.save(user);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("when request /api/users for get all users then return user json")
    public void testGetAllUser() throws Exception {

        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"surname\":\"Samatov\",\"email\":\"admin@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Milatik\"}]"));
    }

    @Test
    @DisplayName("when request /api/user/1 for get user then return user json")
    public void testGetUser() throws Exception {

        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"surname\":\"Samatov\",\"email\":\"admin@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Milatik\"}"));
    }

    @Test
    @DisplayName("when request /api/users for add user then return user json")
    public void testAddUser() throws Exception {

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(
                        "{\"id\":2,\"surname\":\"Samatova\",\"email\":\"admin1@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Zamira\"}"
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":2,\"surname\":\"Samatova\",\"email\":\"admin1@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Zamira\"}"));
    }

    @Test
    @DisplayName("when request /api/users/1 for update user then return user json")
    public void testUpdateUserById() throws Exception {

        mockMvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(
                        "{\"id\":1,\"surname\":\"Samatov\",\"email\":\"admin1@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Ruslan\"}"
                ))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"surname\":\"Samatov\",\"email\":\"admin1@mail.ru\",\"age\":30,\"password\":\"100\",\"roles\":[{\"id\":1,\"name\":\"ROLE_ADMIN\"}],\"name\":\"Ruslan\"}"));
    }

    @Test
    @DisplayName("when request /api/users/1 for delete user then return user json")
    public void testDeleteUserById() throws Exception {

        mockMvc.perform(delete("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("when request /api/users/about-user then return user json")
    public void testShowUserPage() throws Exception {

        mockMvc.perform(get("/api/users/about-user").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("admin@mail.ru"));
    }
    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }
}