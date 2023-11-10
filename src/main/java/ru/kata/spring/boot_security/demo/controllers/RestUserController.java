package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;

@RestController
public class RestUserController {

    private final SecurityUserService securityUserService;

    @Autowired
    public RestUserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        try {
            return ResponseEntity.ok(securityUserService.getUser(principal.getName()));
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
