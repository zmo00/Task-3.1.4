package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.database.UserServiceInt;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserServiceInt userServiceInt;

    @Autowired
    public UserValidator(UserServiceInt userServiceInt) {
        this.userServiceInt = userServiceInt;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> userOptional = userServiceInt.findByUsername(user.getUsername());

        //username
        if (user.getUsername().length() == 0 || user.getUsername().length() > 100) {
            errors.rejectValue("username", "", "Incorrect username");
        }
        if (userOptional.isPresent() && userOptional.get().getId() != user.getId()) {
            errors.rejectValue("username", "", "This username is already occupied");
        }

        //password
        if (user.getPassword().length() == 0 || user.getPassword().length() > 100) {
            errors.rejectValue("password", "", "Incorrect password");
        }

        //firstname
        if (user.getFirstname().length() == 0 || user.getFirstname().length() > 100) {
            errors.rejectValue("firstname", "", "Incorrect firstname");
        }

        //lastname
        if (user.getLastname().length() == 0 || user.getLastname().length() > 100) {
            errors.rejectValue("lastname", "", "Incorrect lastname");
        }

        //email
        if (user.getLastname().length() == 0 || user.getLastname().length() > 100 || !user.getEmail().contains("@")) {
            errors.rejectValue("email", "", "Incorrect email");
        }
    }
}
