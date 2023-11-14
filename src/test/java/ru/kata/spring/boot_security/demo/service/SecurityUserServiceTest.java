package ru.kata.spring.boot_security.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityUserService securityUserService;

    @Test
    @DisplayName("when called loadUserByUsername then success")
    void testLoadUserByUsername() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(
                1, "milatik", "samatop", 28, "halfmsk@gmail.com", "12345", roles);
        var userDetails =
                new org.springframework.security.core.userdetails.
                        User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails result = securityUserService.loadUserByUsername(user.getEmail());

        assertNotNull(result);
        assertEquals(userDetails, result);

        verify(userRepository).findByEmail(any());
    }

    @Test
    @DisplayName("when called loadUserByUsername then UserNotFoundException")
    void testLoadUserByUsernameWhenUserNotFound() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(1, "milatik", "samatop", 28, "halfmsk@gmail.com", "12345", roles);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,()-> securityUserService.loadUserByUsername(user.getEmail()));

        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("when called getUser then success")
    void testGetUser() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(1, "milatik", "samatop", 28, "halfmsk@gmail.com", "12345", roles);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertEquals(user, securityUserService.getUser(user.getEmail()));

        verify(userRepository).findByEmail(any());
    }

    @Test
    @DisplayName("when called getUser then UserNotFoundException")
    void testGetUserNotFound() {

        when(userRepository.findByEmail("abc")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> securityUserService.getUser("abc"));

        verify(userRepository).findByEmail("abc");
    }

    @Test
    @DisplayName("when called getCurrent then success")
    void testGetCurrentUser() {
        var roles = List.of(new Role(1, "USER"), new Role(2, "ADMIN"));
        var user = new User(1, "milatik", "samatop", 28, "halfmsk@gmail.com", "12345", roles);
        var securityUser = new org.springframework.security.core.userdetails
                .User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertEquals(user, securityUserService.getCurrentUser());

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
        verify(userRepository).findByEmail(any());
    }

    @Test
    @DisplayName("when called getCurrent then UserNotFoundException")
    void testGetCurrentUserNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> securityUserService.getCurrentUser());

        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
