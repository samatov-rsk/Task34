package ru.kata.spring.boot_security.demo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kata.spring.boot_security.demo.BaseIT;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest extends BaseIT {

    @BeforeEach
    public void testClearDataBase() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("when findAllUser called then success")
    public void testFindAllUser() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User("a", "aa", "gmail", 25, "aaaa", roles);
        var user2 = new User("a", "aa", "mail", 25, "aaaa", roles);

        var users = List.of(user, user2);

        userRepository.saveAll(users);
        List<User> result = userRepository.findAll();

        assertEquals(users.size(), result.size());
        assertEquals(users.get(0).getName(), result.get(0).getName());
        assertEquals(users.get(0).getSurname(), result.get(0).getSurname());
    }

    @Test
    @DisplayName("when findAllUsers called then not found")
    public void testFindAllUserNotFound() {
        var users = userRepository.findAll();
        assertEquals(0, users.size());
    }

    @Test
    @DisplayName("when findById called then success")
    public void testFindById() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User("a", "aa", "gmail", 25, "aaaa", roles);

        User saved = userRepository.save(user);

        assertEquals(Optional.of(user), userRepository.findById(saved.getId()));
    }

    @Test
    @DisplayName("when findById called  then not found")
    public void testFindByIdNotFound() {

        assertEquals(Optional.empty(), userRepository.findById(5));

    }

    @Test
    @DisplayName("when save called then success")
    public void testSaveUser() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User("a", "aa", "gmail", 25, "aaaa", roles);

        assertEquals(user, userRepository.save(user));

    }

    @Test
    @DisplayName("when delete called then success ")
    public void testDeleteUser() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);

        userRepository.save(user);

        assertEquals(Optional.empty(), userRepository.findById(user.getId()));

    }

    @Test
    @DisplayName("when delete called then not found")
    public void testDeleteUserNotFound() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);

        assertEquals(Optional.empty(), userRepository.findById(user.getId()));

    }
}
