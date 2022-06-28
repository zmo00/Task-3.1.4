package ru.kata.spring.boot_security.demo.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.RoleServiceInt;
import ru.kata.spring.boot_security.demo.services.database.UserServiceInt;

import java.util.Collections;
import java.util.Optional;

@Service
public class RegistrationService {

    private final UserServiceInt userServiceInt;

    private final RoleServiceInt roleServiceInt;

    @Autowired
    public RegistrationService(UserServiceInt userServiceInt, RoleServiceInt roleServiceInt) {
        this.userServiceInt = userServiceInt;
        this.roleServiceInt = roleServiceInt;
    }

    public void register(User user) {
        Optional<Role> roleOptional = roleServiceInt.findByRole("ROLE_USER");
        if (roleOptional.isPresent()) {
            Role userRole = roleOptional.get();
            userRole.getUsers().add(user);
            user.setRoles(Collections.singletonList(userRole));
            roleServiceInt.update(userRole);
            userServiceInt.create(user);
        } else {
            Role userRole = new Role("ROLE_USER");
            userRole.setUsers(Collections.singletonList(user));
            user.setRoles(Collections.singletonList(userRole));
            roleServiceInt.create(userRole);
            userServiceInt.create(user);
        }
    }

}
