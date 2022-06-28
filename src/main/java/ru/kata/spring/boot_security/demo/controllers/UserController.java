package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.services.database.UserServiceInt;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserServiceInt userServiceInt;

    @Autowired
    public UserController(UserServiceInt userServiceInt) {
        this.userServiceInt = userServiceInt;
    }

    @GetMapping
    public String userPage(Model model, Authentication authentication) {
        model.addAttribute("user", userServiceInt.findByUsername(authentication.getName()).get());
        return "/user/userPage";
    }

}
