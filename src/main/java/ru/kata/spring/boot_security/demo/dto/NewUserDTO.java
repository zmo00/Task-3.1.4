package ru.kata.spring.boot_security.demo.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

@Getter
@Setter
public class NewUserDTO {

    private String username;

    private String password;

    private String firstname;

    private String lastname;

    private String email;

    private int[] roles;

    public NewUserDTO() {

    }

    public NewUserDTO(String username, String password, String firstname, String lastname, String email, int[] roles) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles;
    }

    public User convertToUser() {
        return new User(username, password, firstname, lastname, email);
    }

}
