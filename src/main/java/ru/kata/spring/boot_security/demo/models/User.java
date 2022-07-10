package ru.kata.spring.boot_security.demo.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class User implements UserDetails {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn
    )
    private List<Role> roles;

    public User() {

    }

    public User(String username, String password, String firstname, String lastname, String email) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;

        this.roles = new ArrayList<>();
    }

    @Override
    public String toString() {
        return username + "{" +
                lastname + "_" +
                firstname.charAt(0) + "}";
    }

    public String getRolesAsString() {
        if (roles.size() == 1) {
            return roles.get(0).getRoleWithoutPrefix();
        } else {
            StringBuilder roleList = new StringBuilder(roles.get(0).getRoleWithoutPrefix());
            for (int i = 1; i < roles.size(); i++) {
                roleList.append(", ")
                        .append(roles.get(i).getRoleWithoutPrefix());
            }
            return roleList.toString();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
