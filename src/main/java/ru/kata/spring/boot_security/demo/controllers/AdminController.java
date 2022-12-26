package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utils.UserValidator;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, UserValidator userValidator, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/index")
    public String showHome(Principal principal, Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("rolesList", roleService.listRoles());
        return "/admin/index";
    }

    @GetMapping("/{id}/showFormForEdit")
    public String showFormForEdit(@PathVariable("id") long id, Model model) {
        User userToEdit = userService.getUserById(id);
        model.addAttribute("editUser", userToEdit);
        model.addAttribute("roles", roleService.listRoles());
        return "/admin/user-form";
    }

    @PostMapping("/update/{id}")
    public String update(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin/index";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUserById(id);
        return "redirect:/admin/index";
    }

    @PatchMapping("/save")
    public String processRegistration(@ModelAttribute("newuser") @Valid User user, BindingResult bindingResult, Model model) {
        model.addAttribute("roles", roleService.listRoles());
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/admin/add-user";
        }
        userService.saveUser(user);
        return "redirect:/admin/index";
    }
}
