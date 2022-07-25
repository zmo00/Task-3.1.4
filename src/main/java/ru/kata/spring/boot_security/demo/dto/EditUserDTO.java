package ru.kata.spring.boot_security.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUserDTO {

    long id;

    String username;

    String firstname;

    String lastname;

    String email;

    int[] roles;

    public EditUserDTO() {
    }

    public EditUserDTO(long id, String username, String firstname, String lastname, String email, int[] roles) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.roles = roles;
    }

}
