package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.utils.UserErrorResponse;
import ru.kata.spring.boot_security.demo.utils.UserNotCreatedException;
import ru.kata.spring.boot_security.demo.utils.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AppRestController {
    private final UserService userService;
    private final RoleService roleService;


    public AppRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/sayHello")
    public String sayHello() {
        return "FUking jSON!";
    }

    @GetMapping("/userlist")
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User temp = userService.getUserById(id);
        return new ResponseEntity<>(temp, HttpStatus.OK);
    }

    @PostMapping("/adduser")
    public ResponseEntity<UserErrorResponse> newUser(@RequestBody User user) {
        if (userService.isExistEmail(user)){
            return new ResponseEntity<>(new UserErrorResponse("Email already exists. \n Use another.", System.currentTimeMillis()), HttpStatus.BAD_REQUEST);
        } else
            userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/updateUser/{id}")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User temp = userService.getUserById(id);
        if (temp == null) {
            throw new UserNotFoundException();
        }
        userService.removeUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> showUser(Authentication auth) {
        return new ResponseEntity<>(userService.findByUsername(auth.getName()), HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handlerException(UserNotFoundException e) {
        UserErrorResponse response = new UserErrorResponse("ID not found! Fuck!", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handlerException(UserNotCreatedException e) {
        UserErrorResponse response = new UserErrorResponse("Can't create User! Fuck!" + e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.getUserRoles(), HttpStatus.OK);
    }

}


//    @PostMapping("/adduser")
//    public User addUser(@RequestBody  @Valid User user, BindingResult bindingResult) {
//        user.setId(0);
//        if (bindingResult.hasErrors()) {
//            StringBuilder errMsg = new StringBuilder();
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                errMsg.append(error.getField())
//                        .append(" - ").append(error.getDefaultMessage())
//                        .append(";");
//            }
//            throw new UserNotCreatedException(errMsg.toString());
//        }
//        userService.saveUser(user);
//        return user;
//    }