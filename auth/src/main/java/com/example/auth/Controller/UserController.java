package com.example.auth.Controller;


import com.example.auth.Dto.UserDto;
import com.example.auth.Entity.User;
import com.example.auth.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userlist")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody UserDto userDto){
        ResponseEntity responseEntity = userService.signup(userDto);
        return responseEntity;
    }

    @PostMapping("/hello")
    public String hello(){
        return "hello";
    }



}
