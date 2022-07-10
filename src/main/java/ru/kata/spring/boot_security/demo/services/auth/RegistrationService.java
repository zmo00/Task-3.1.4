package ru.kata.spring.boot_security.demo.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.RoleService;
import ru.kata.spring.boot_security.demo.services.database.UserService;

import java.util.List;

@Service
public class RegistrationService {

    private final UserService userService;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userServiceInt, RoleService roleServiceInt, PasswordEncoder passwordEncoder) {
        this.userService = userServiceInt;
        this.roleService = roleServiceInt;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roleList = user.getRoles();
        for (Role role : roleList) {
            role.getUsers().add(user);
        }

        user.setRoles(roleList);

        for (Role role : roleList) {
            roleService.update(role);
        }
        userService.create(user);
    }

}
