package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.auth.RegistrationService;
import ru.kata.spring.boot_security.demo.services.database.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userServiceInt;

    private final UserValidator userValidator;

    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserService userServiceInt, UserValidator userValidator, RegistrationService registrationService) {
        this.userServiceInt = userServiceInt;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String adminPage(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("userAll", userServiceInt.readAll());
        model.addAttribute("roleAdmin", "ROLE_ADMIN");
        return "admin/adminPage";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }

        registrationService.register(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userServiceInt.read(id));
        return "admin/showUser";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userServiceInt.read(id));
        return "admin/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id, BindingResult bindingResult) {
        user.setRoles(userServiceInt.read(user.getId()).getRoles());
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }

        user.setRoles(userServiceInt.read(id).getRoles());
        userServiceInt.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userServiceInt.delete(userServiceInt.read(id));
        return "redirect:/admin";
    }

}
