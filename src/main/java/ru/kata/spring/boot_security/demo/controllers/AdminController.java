package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.auth.RegistrationService;
import ru.kata.spring.boot_security.demo.services.database.RoleService;
import ru.kata.spring.boot_security.demo.services.database.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserService userServiceInt, RoleService roleService, RegistrationService registrationService) {
        this.userService = userServiceInt;
        this.roleService = roleService;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String adminPage(Model model, Authentication authentication) {
        model.addAttribute("admin", userService.findByUsername(authentication.getName()).orElseThrow());
        model.addAttribute("editUser", new User());
        model.addAttribute("userList", userService.readAll());
        model.addAttribute("roleList", roleService.readAll());
        return "adminPage";
    }

    @PostMapping
    public String create(@ModelAttribute("deleteUser") @Valid User newUser) {
        registrationService.register(newUser);
        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("editUser") User user, @PathVariable("id") long id) {
        user.setPassword(userService.read(id).getPassword());
        userService.update(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.delete(userService.read(id));
        return "redirect:/admin";
    }

}
