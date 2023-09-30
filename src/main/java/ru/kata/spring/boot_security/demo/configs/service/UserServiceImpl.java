package ru.kata.spring.boot_security.demo.configs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;

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
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found by %s" + userId));
    }

    @Transactional
    @Override
    public void addUser(User user) {
        List<Role> userRoles = roleRepository.findAllByNameIn(user.getRoles()
                .stream().
                map(Role::getName).
                collect(Collectors.toList()));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Integer userId) {
        userRepository.delete(userRepository.getById(userId));
    }

    @Transactional
    @Override
    public void userToUpdate(Integer userId, User user) {
        User existingUser = getUserById(userId);

        existingUser.setName(user.getName());
        existingUser.setSurname(user.getSurname());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());
        List<Role> existingRoles = roleRepository.findAllByNameIn(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
        existingUser.setRoles(existingRoles);
        userRepository.save(existingUser);
    }
}
