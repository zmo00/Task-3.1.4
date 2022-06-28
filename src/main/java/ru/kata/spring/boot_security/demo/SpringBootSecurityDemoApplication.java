package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.RoleServiceInt;
import ru.kata.spring.boot_security.demo.services.database.UserServiceInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
		UserServiceInt userServiceInt = context.getBean(UserServiceInt.class);
		RoleServiceInt roleServiceInt = context.getBean(RoleServiceInt.class);
		if (userServiceInt.readAll().isEmpty() && roleServiceInt.readAll().isEmpty()) {
			User admin = new User("admin", "admin", "adminFirstname", "userLastname", "admin@mail.ru");
			User user = new User("user", "user", "userFirstname", "userLastname", "user@mail.ru");

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
