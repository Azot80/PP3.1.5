package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User getUserById(Long id);
    void saveUser(User user);
    void removeUserById(Long id);
    List<User> getAllUsers();
    User findByUsername(String username);
    boolean isNameExist(User user);
    User getUserByEmail(String email);
    List<Role> getUserRoles();
    User findByEmail(String email);
    boolean isExistEmail(User user);
}
