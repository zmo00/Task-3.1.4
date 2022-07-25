package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.NewUserDTO;
import ru.kata.spring.boot_security.demo.dto.EditUserDTO;
import ru.kata.spring.boot_security.demo.dto.RoleDTO;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.auth.RegistrationService;
import ru.kata.spring.boot_security.demo.services.database.RoleService;
import ru.kata.spring.boot_security.demo.services.database.UserService;

import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/api/getUsers")
    @ResponseBody
    public List<UserDTO> getUserList() {
        List<User> userList = userService.readAll();
        List<UserDTO> userDtoList = new ArrayList<>();

        for (User user : userList) {
            userDtoList.add(new UserDTO(user));
        }

        return userDtoList;
    }

    @GetMapping("/api/getRoles")
    @ResponseBody
    public List<RoleDTO> getRoleList() {
        List<Role> roleList = roleService.readAll();
        List<RoleDTO> roleDtoList = new ArrayList<>();

        for (Role role : roleList) {
            roleDtoList.add(new RoleDTO(role));
        }

        return roleDtoList;
    }

    @GetMapping("/api/getUserById/{id}")
    @ResponseBody
    public UserDTO getUser(@PathVariable("id") long id) {
        return new UserDTO(userService.read(id));
    }

    @PostMapping("/api/addNewUser")
    public ResponseEntity<?> create(@RequestBody NewUserDTO newUserDTO) {
        User newUser = newUserDTO.convertToUser();

        List<Role> roleList = new ArrayList<>();
        for (int roleId : newUserDTO.getRoles()) {
            roleList.add(roleService.read(roleId));
        }

        newUser.setRoles(roleList);

        registrationService.register(newUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/api/editUser/{id}")
    public ResponseEntity<?> update(@RequestBody EditUserDTO editUserDTO) {
        userService.update(convertToUser(editUserDTO));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/deleteUser/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        userService.delete(userService.read(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private User convertToUser(EditUserDTO editUserDTO) {
        User userBefore = userService.read(editUserDTO.getId());

        List<Role> roleList = new ArrayList<>();
        for (int roleId : editUserDTO.getRoles()) {
            roleList.add(roleService.read(roleId));
        }

        User userAfter = new User();

        userAfter.setId(userBefore.getId());
        userAfter.setUsername(editUserDTO.getUsername());
        userAfter.setPassword(userBefore.getPassword());
        userAfter.setFirstname(editUserDTO.getFirstname());
        userAfter.setLastname(editUserDTO.getLastname());
        userAfter.setEmail(editUserDTO.getEmail());
        userAfter.setRoles(roleList);

        return userAfter;
    }

}
