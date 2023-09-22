package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.kata.spring.boot_security.demo.configs.service.UserService;
import ru.kata.spring.boot_security.demo.models.User;

import java.security.Principal;

@CrossOrigin(origins = "http://localhost:8089/admin", methods = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/admin")
@Controller
public class AdminController {
    UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String showUser(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.findByEmail(email);
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("userRoles", user.getRoles());
        return "admin";
    }

}
