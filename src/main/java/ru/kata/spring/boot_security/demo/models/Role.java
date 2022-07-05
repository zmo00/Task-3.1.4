package ru.kata.spring.boot_security.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Role implements GrantedAuthority {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    public Role() {
    }

    public Role(String role) {
        this.role = role;

        this.users = new ArrayList<>();
    }

    @Override
    public String toString() {
        return role;
    }

    public String getRoleWithoutPrefix() {
        StringBuilder s = new StringBuilder(role);
        return s.delete(0, 5).toString();
    }

    @Override
    public String getAuthority() {
        return role;
    }
}