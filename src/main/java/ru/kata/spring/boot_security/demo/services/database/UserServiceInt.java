package ru.kata.spring.boot_security.demo.services.database;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceInt {

    void create(User user);

    User read(long id);

    void update(User user);

    void delete(User user);

    List<User> readAll();

    Optional<User> findByUsername(String username);

}
