package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

public class AppController {

    @GetMapping("/showUserInfo")
    public String show() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) auth.getPrincipal();
        System.out.println(userDetails.toString());
        for (Role role : userDetails.getRoles()) {
            System.out.println(role.getRoleName());
        }
        return "/admin/index";
    }
}
