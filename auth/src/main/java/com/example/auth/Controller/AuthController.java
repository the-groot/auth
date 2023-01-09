package com.example.auth.Controller;

import com.example.auth.Dto.UserDto;
import com.example.auth.Service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody UserDto userDto){
        authService.loginCheck(userDto);
    }
}
