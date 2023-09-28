package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositiories.UserRepository;

import java.security.Principal;

@RequestMapping("/admin")
@Controller
public class AdminController {

    private final UserRepository userRepository;

    @Autowired
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getUserPage(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRoles", user.getRoles());
        return "admin";
    }
}
