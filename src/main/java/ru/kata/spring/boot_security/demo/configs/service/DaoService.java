package ru.kata.spring.boot_security.demo.configs.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Service
public interface DaoService {

    List<User> getAllUsers();

    User getUserById(long id);

    void addUser(User user);

    void removeUser(long id);

    void updateUser(Long id, User user);
}
