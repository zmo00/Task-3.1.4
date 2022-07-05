package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.services.database.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userServiceInt;

    @Autowired
    public UserController(UserService userServiceInt) {
        this.userServiceInt = userServiceInt;
    }

    @GetMapping
    public String userPage(Model model, Authentication authentication) {
        model.addAttribute("user", userServiceInt.findByUsername(authentication.getName()).get());
        return "/user/userPage";
    }

}
