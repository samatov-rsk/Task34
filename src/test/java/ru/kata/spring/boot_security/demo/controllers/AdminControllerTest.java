package ru.kata.spring.boot_security.demo.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import ru.kata.spring.boot_security.demo.BaseIT;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AdminControllerTest extends BaseIT {

    @Autowired
    SecurityUserService securityUserService;

    @BeforeEach
    public void setUser() {
        User user = new User();
        user.setEmail("admin@mail.ru");
        user.setRoles(List.of(new Role(1, "ROLE_ADMIN")));
        user.setPassword("100");
        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("when request /user then return user json")
    public void testShowUser() throws Exception {
        this.mockMvc.perform(get("/admin")).andExpect(
                status().isOk()).andExpect(view().name("admin"));
    }

    @AfterEach
    public void clearContext(){
        SecurityContextHolder.clearContext();
    }
}
