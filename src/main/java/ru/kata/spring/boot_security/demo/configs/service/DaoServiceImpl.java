package ru.kata.spring.boot_security.demo.configs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public class DaoServiceImpl implements DaoService {

    private final UserDao userDao;

    @Autowired
    public DaoServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Transactional
    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Transactional
    @Override
    public void removeUser(Integer userId) {
        userDao.removeUser(userId);
    }

    @Transactional
    @Override
    public void updateUser(Integer userId, User user) {
        userDao.updateUser(userId, user);
    }
}
