package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.exception.EmailNotUniqueException;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8089/admin", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/users")
public class RestAdminController {

    private final UserServiceImpl userService;
    private final SecurityUserService securityUserService;

    @Autowired
    public RestAdminController(UserServiceImpl userService, SecurityUserService securityUserService) {
        this.userService = userService;
        this.securityUserService = securityUserService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.addUser(user));
        }catch (EmailNotUniqueException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Integer userId, @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.updateUserById(userId, user));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        try {
            userService.removeUser(userId);
            return ResponseEntity.noContent().build();
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/about-user")
    public ResponseEntity<User> getCurrentUser() {
        try {
            return ResponseEntity.ok(securityUserService.getCurrentUser());
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
