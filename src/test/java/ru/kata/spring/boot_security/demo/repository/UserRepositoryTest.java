package ru.kata.spring.boot_security.demo.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kata.spring.boot_security.demo.BaseIT;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest extends BaseIT {

    @Test
    @DisplayName("when find All user then success")
    public void testFindAllUser() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);
        var user2 = new User(2, "a", "aa", 25, "mail", "aaaa", roles);

        var users = List.of(user, user2);

        userRepository.saveAll(users);
        List<User> allUsers = userRepository.findAll();

        assertEquals(2, allUsers.size());
        User testUser = allUsers.get(1);
        assertEquals("a", testUser.getName());
        assertEquals("aa", testUser.getSurname());
    }

    @Test
    @DisplayName("when save user then success")
    public void testSaveUser() {

        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);

        assertEquals(user, userRepository.save(user));
    }

    @Test
    @DisplayName("when find by Id user then success")
    public void testFindById(){
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);

        userRepository.save(user);

        assertEquals(Optional.of(user),userRepository.findById(1));
    }

    @Test
    @DisplayName("when delete user then success ")
            public void testDeleteUser() {
        var roles = List.of(new Role(1, "ADMIN"));
        var user = new User(1, "a", "aa", 25, "gmail", "aaaa", roles);

        userRepository.save(user);

        userRepository.delete(user);

        assertEquals(Optional.empty(),userRepository.findById(user.getId()));
    }
}
