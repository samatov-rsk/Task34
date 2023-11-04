package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.kata.spring.boot_security.demo.configs.SuccessUserHandler;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;

import java.security.Principal;

@WebMvcTest(controllers = {UserController.class, AdminController.class})
@Import(value = {SuccessUserHandler.class, SecurityUserService.class})
abstract public class BaseWeb {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected SecurityUserService securityUserService;

    @MockBean
    protected UserRepository userRepository;

    @MockBean
    protected RestUserController restUserController;

    @MockBean
    protected Principal principal;

}
