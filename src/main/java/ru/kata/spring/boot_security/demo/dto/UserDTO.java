package ru.kata.spring.boot_security.demo.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO {

    private long number;

    private String nameOnServer;

    private String firstname;

    private String lastname;

    private String email;

    private List<RoleDTO> roles;

    public UserDTO(User user) {
        this.number = user.getId();
        this.nameOnServer = user.getUsername();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();

        this.roles = user.getRoles().stream()
                .map(RoleDTO::new)
                .collect(Collectors.toList());
    }

}
