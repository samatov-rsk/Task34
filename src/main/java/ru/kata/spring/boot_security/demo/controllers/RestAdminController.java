package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

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
            return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
            return ResponseEntity.ok(userService.addUser(user));
        }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable Integer userId, @RequestBody User user) {
            return ResponseEntity.ok(userService.updateUserById(userId, user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
            userService.removeUser(userId);
            return ResponseEntity.noContent().build();
    }

    @GetMapping("/about-user")
    public ResponseEntity<User> getCurrentUser() {
            return ResponseEntity.ok(securityUserService.getCurrentUser());
    }

}
