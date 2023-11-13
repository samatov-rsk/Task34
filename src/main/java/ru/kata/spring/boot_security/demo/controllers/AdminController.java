package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import ru.kata.spring.boot_security.demo.exception.UserNotFoundException;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final SecurityUserService securityUserService;

    @Autowired
    public AdminController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping
    public String getUserPage(Model model, Principal principal) {
            User user = securityUserService.getUser(principal.getName());
            model.addAttribute("userEmail", user.getEmail());
            model.addAttribute("userRoles", user.getRoles());
            return "admin";
    }

}
