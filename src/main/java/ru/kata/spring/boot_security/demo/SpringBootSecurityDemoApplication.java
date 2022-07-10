package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.RoleService;
import ru.kata.spring.boot_security.demo.services.database.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

		UserService userServiceInt = context.getBean(UserService.class);
		RoleService roleServiceInt = context.getBean(RoleService.class);
		PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

		if (userServiceInt.readAll().isEmpty() && roleServiceInt.readAll().isEmpty()) {
			User admin = new User("admin", "admin", "adminFirstname", "adminLastname", "admin@mail.ru");
			User user = new User("user", "user", "userFirstname", "userLastname", "user@mail.ru");

			admin.setPassword(passwordEncoder.encode(admin.getPassword()));
			user.setPassword(passwordEncoder.encode(user.getPassword()));

			Role adminRole = new Role("ROLE_ADMIN");
			Role userRole = new Role("ROLE_USER");

			adminRole.setUsers(new ArrayList<>(Collections.singletonList(admin)));
			userRole.setUsers(new ArrayList<>(List.of(admin, user)));

			admin.setRoles(new ArrayList<>(List.of(adminRole, userRole)));
			user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));

			roleServiceInt.create(adminRole);
			roleServiceInt.create(userRole);

			userServiceInt.create(admin);
			userServiceInt.create(user);
		}
	}

}
