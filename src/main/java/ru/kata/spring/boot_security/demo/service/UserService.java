package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Integer userId);

    User addUser(User user);

    void removeUser(Integer userId);

    User updateUserById(Integer userId, User user);
}
