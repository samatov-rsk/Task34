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
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiory.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    SecurityUserService securityUserService;


    @Test
    void loadUserByUsername() {
    }

    @Test
    @DisplayName("when get user then success")
    void testGetUser() {
        var roles = List.of(new Role(1,"USER"),new Role(2,"ADMIN"));
        var user = new User(1,"milatik","samatop",28,"halfmsk@gmail.com","12345",roles);
        when(userRepository.findByEmail(any())).thenReturn(user);

        assertEquals(user,securityUserService.getUser(user.getEmail()));

        verify(userRepository).findByEmail(any());
    }

    @Test
    @DisplayName("when get user not found then success")
    void testGetUserNotFound() {
        var user = new User();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        assertThrows(UserNotFoundException.class,()->securityUserService.getUser(user.getEmail()));
        verify(userRepository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("when get current user then success")
    void testGetCurrentUser() {
        var roles = List.of(new Role(1,"USER"),new Role(2,"ADMIN"));
        var user= new User(1,"milatik","samatop",28,"halfmsk@gmail.com","12345",roles);
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        User currentUser = securityUserService.getCurrentUser();

        assertEquals(user,currentUser);

        verify(userRepository).findByEmail(any());
    }

    @Test
    @DisplayName("when get current user not found then success")
    void testGetCurrentUserNotFound() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);

        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(null);

        assertThrows(UserNotFoundException.class,()->securityUserService.getCurrentUser());

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
