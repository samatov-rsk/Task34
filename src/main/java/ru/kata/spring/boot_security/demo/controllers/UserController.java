package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.service.SecurityUserService;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;

@Controller
public class UserController {

    private final SecurityUserService securityUserService;

    @Autowired
    public UserController(SecurityUserService securityUserService) {
        this.securityUserService = securityUserService;
    }

    @GetMapping("/user")
    public String showUser(Model model, Principal principal) {
        User user = securityUserService.getUser(principal.getName());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRoles", user.getRoles());
        return "user";
    }
}
