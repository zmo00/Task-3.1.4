package ru.kata.spring.boot_security.demo.services.database;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    void create(Role role);

    Role read(int id);

    void update(Role role);

    void delete(Role role);

    List<Role> readAll();

    Optional<Role> findByRole(String role);

}
