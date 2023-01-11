package com.example.auth.Service;

import com.example.auth.Dto.LoginDto;
import com.example.auth.Repository.UserRepository;
import com.example.auth.Security.TokenInfo;
import com.example.auth.Security.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider) {

        this.authenticationManagerBuilder=authenticationManagerBuilder;
        this.tokenProvider=tokenProvider;
    }

    public TokenInfo login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfo jwt = tokenProvider.createToken(authentication);

        return jwt;
    }

}