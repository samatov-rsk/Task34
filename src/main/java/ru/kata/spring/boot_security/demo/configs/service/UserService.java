package ru.kata.spring.boot_security.demo.configs.service;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Integer userId);

    void addUser(User user);

    void removeUser(Integer userId);

    void userToUpdate(Integer userId, User user);
}
