package ru.kata.spring.boot_security.demo.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.RoleService;
import ru.kata.spring.boot_security.demo.services.database.UserService;

import java.util.Collections;
import java.util.Optional;

@Service
public class RegistrationService {

    private final UserService userServiceInt;

    private final RoleService roleServiceInt;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserService userServiceInt, RoleService roleServiceInt, PasswordEncoder passwordEncoder) {
        this.userServiceInt = userServiceInt;
        this.roleServiceInt = roleServiceInt;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleServiceInt.findByRole("ROLE_USER").orElse(new Role("ROLE_USER"));
        userRole.getUsers().add(user);
        user.setRoles(Collections.singletonList(userRole));

        roleServiceInt.update(userRole);
        userServiceInt.create(user);
    }

}
