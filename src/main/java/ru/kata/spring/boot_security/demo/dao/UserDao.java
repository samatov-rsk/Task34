package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    User getUserById(Integer userId);

    void addUser(User user);

    void removeUser(Integer userId);

    void updateUser(Integer userId, User user);
}
