package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDaoImpl(UserRepository repository, RoleRepository roleRepository) {
        this.userRepository = repository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public void addUser(User user) {
        List<Role> userRoles = roleRepository.findAllByNameIn(user.getRoles()
                .stream().map(Role::getName).collect(Collectors.toList()));
        user.setRoles(userRoles);
        userRepository.save(user);
    }

    @Override
    public void removeUser(long id) {
        userRepository.delete(userRepository.getById(id));
    }

    @Override
    public void updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);

        existingUser.setName(updatedUser.getName());
        existingUser.setSurname(updatedUser.getSurname());
        existingUser.setAge(updatedUser.getAge());
        existingUser.setEmail(updatedUser.getEmail());
        List<Role> existingRoles = roleRepository.findAllByNameIn(
                updatedUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
        existingUser.setRoles(existingRoles);
        userRepository.save(existingUser);
    }

}
