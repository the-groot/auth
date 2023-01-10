package com.example.auth.Controller;

import com.example.auth.Dto.UserDto;
import com.example.auth.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(AuthService authService, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.authService = authService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }



//    @PostMapping("/sex")
//    public void login(@Valid @RequestBody UserDto userDto){
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
//                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());
//
//        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
//        System.out.println("authenticate = " + authenticate);
//
//
//    }

    @PostMapping("/authenticate")
    public String authenticate(@Valid @RequestBody UserDto userDto){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
               new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword());

       authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return "ㅗ디ㅣㅐ";
    }
}
