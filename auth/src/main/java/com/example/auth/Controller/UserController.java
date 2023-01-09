package com.example.auth.Controller;


import com.example.auth.Service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

  //  @GetMapping("/userlist")
//    public List<User> getAllUsers(){
//        return userService.getAllUsers();
//    }
}
