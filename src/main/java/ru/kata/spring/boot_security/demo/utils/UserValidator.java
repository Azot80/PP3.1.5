package ru.kata.spring.boot_security.demo.utils;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.DBUserDetailsService;

@Component
public class UserValidator implements Validator {

    private final DBUserDetailsService userDetailsService;

    public UserValidator(DBUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        try {
            userDetailsService.loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException ignored) {
            return; // пользователь не найден
        }
        errors.rejectValue("username", "", "Имя уже занято");
    }
}
