package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.configs.service.UserService;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;

@RestController
public class RestUserController {

    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
