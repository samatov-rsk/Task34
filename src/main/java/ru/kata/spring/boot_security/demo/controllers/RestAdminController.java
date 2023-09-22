package ru.kata.spring.boot_security.demo.controllers;

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
import ru.kata.spring.boot_security.demo.configs.service.UserService;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8089/admin", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/users")
public class RestAdminController {

    private final UserDaoImpl userDao;
    private final UserService userService;

    public RestAdminController(UserDaoImpl userDao, UserService userService) {
        this.userDao = userDao;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userDao.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userDao.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userDao.addUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> updateUsers(@PathVariable Long id, @RequestBody User updatedUser) {
        userDao.updateUser(id, updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userDao.removeUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/about-user")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}