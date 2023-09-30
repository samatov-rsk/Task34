package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.kata.spring.boot_security.demo.configs.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.configs.service.UserServiceImpl;
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
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> userToUpdate(@PathVariable Integer userId, @RequestBody User user) {
        userService.userToUpdate(userId, user);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.removeUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/about-user")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = securityUserService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }
}
