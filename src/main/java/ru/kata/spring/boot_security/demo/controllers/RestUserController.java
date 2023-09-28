package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;

import java.security.Principal;

@RestController
public class RestUserController {

    private final UserRepository userRepository;

    public RestUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }
}
