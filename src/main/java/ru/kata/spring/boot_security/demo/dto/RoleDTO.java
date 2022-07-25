package ru.kata.spring.boot_security.demo.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kata.spring.boot_security.demo.models.Role;

@Getter
@Setter
public class RoleDTO {

    int id;

    String roleWithoutPrefix;

    public RoleDTO(Role role) {
        this.id = role.getId();
        this.roleWithoutPrefix = role.getRoleWithoutPrefix();
    }

    @Override
    public String toString() {
        return roleWithoutPrefix;
    }

    public String getRoleWithPrefix() {
        return "ROLE_" + roleWithoutPrefix;
    }

}
