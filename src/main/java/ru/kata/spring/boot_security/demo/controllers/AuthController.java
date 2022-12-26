package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error, ModelMap modelMap) {
        modelMap.addAttribute("error", error != null);
        return "/auth/login";
    }

}
