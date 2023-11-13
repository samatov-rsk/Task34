package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiory.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiory.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by userId: " + userId));
    }

    @Transactional
    @Override
    public User addUser(User user) {
        List<Role> userRoles = roleRepository.findAllByNameIn(user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        user.setRoles(userRoles);
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Integer userId) {
        userRepository.delete(getUserById(userId));
    }

    @Transactional
    @Override
    public User updateUserById(Integer userId, User user) {
        User userToUpdate = getUserById(userId);

        userToUpdate.setName(user.getName());
        userToUpdate.setSurname(user.getSurname());
        userToUpdate.setAge(user.getAge());
        userToUpdate.setEmail(user.getEmail());
        List<Role> existingRoles = roleRepository.findAllByNameIn(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()));
        userToUpdate.setRoles(existingRoles);
        return userRepository.save(userToUpdate);
    }
}
