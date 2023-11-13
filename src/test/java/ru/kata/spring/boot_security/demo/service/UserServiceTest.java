package ru.kata.spring.boot_security.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiory.RoleRepository;
import ru.kata.spring.boot_security.demo.repositiory.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("when get all users then success")
    void testGetAllUsers() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user1 = new User(1, "ruslen", "samatov", 25, "samativ@mail.ru", "passsss", roles);
        var user2 = new User(2, "mura", "samatov", 28, "samativ@mail.ru", "passsss", roles);
        var user3 = new User(3, "zama", "samatova", 27, "samativ@mail.ru", "passsss", roles);
        List<User> users = List.of(user1, user2, user3);

        when(userRepository.findAll()).thenReturn(users);

        var result = userService.getAllUsers();

        verify(userRepository).findAll();

        assertNotNull(result);
        assertEquals(userService.getAllUsers(), result);
    }

    @Test
    @DisplayName("when get all users Not found then success")
    void testGetUsersNotFound() {

        when(userRepository.findAll()).thenReturn(List.of());

        assertEquals(0, userService.getAllUsers().size());

        verify(userRepository).findAll();

    }

    @Test
    @DisplayName("when get User by Id then success")
    void testGetUserById() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(1, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.getUserById(user.getId());

        verify(userRepository).findById(user.getId());

        assertEquals(user, result);
    }

    @Test
    @DisplayName("when get User by Id Not found then success")
    void testGetUserByIdWhenUserNotFound() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(5, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(user.getId()));
    }


    @Test
    @DisplayName("when add user then success")
    void testAddUser() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var expected = new User(1, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(roleRepository.findAllByNameIn(any())).thenReturn(roles);
        when(userRepository.save(any())).thenReturn(expected);

        var result = userService.addUser(expected);

        verify(roleRepository).findAllByNameIn(any());
        verify(userRepository).save(any());

        assertNotNull(result);
        assertEquals(roles, result.getRoles());

    }

    @Test
    @DisplayName("when remove user then success")
    void testRemoveUser() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var expected = new User(1, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(userRepository.getById(expected.getId())).thenReturn(expected);
        userService.removeUser(expected.getId());

        verify(userRepository).getById(any());
        verify(userRepository).delete(userRepository.getById(expected.getId()));
    }

    @Test
    @DisplayName("when remove user not found userId then success")
    void testRemoveUserWhenNotFoundUserId() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var expected = new User(5, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(userRepository.findById(expected.getId())).thenReturn(Optional.empty());

        userService.removeUser(expected.getId());

        verify(userRepository).getById(any());
        verify(userRepository).delete(userRepository.getById(expected.getId()));
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(expected.getId()));

    }

    @Test
    @DisplayName("when update user then success")
    void testUpdateUserById() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var expected = new User(1, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(roleRepository.findAllByNameIn(any())).thenReturn(roles);
        when(userRepository.save(expected)).thenReturn(expected);
        when(userRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        var result = userService.updateUserById(expected.getId(), expected);

        verify(roleRepository).findAllByNameIn(any());
        verify(userRepository).save(any());
        verify(userRepository).findById(expected.getId());

        assertNotNull(result);
        assertEquals(roles, result.getRoles());
    }

    @Test
    @DisplayName("when update user not found userId then success")
    void testUpdateUserWhenNotFoundUserId() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var expected = new User(5, "ruslen", "samativ", 25, "samativ@mail.ru", "passsss", roles);

        when(userRepository.findById(expected.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUserById(expected.getId(), expected));

        verify(userRepository).findById(expected.getId());
        verify(roleRepository, never()).findAllByNameIn(any());
        verify(userRepository, never()).save(any());

    }

}
