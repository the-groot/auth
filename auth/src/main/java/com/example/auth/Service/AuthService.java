package com.example.auth.Service;

import com.example.auth.Dto.LoginDto;
import com.example.auth.Entity.RefreshToken;
import com.example.auth.Repository.RedisRepository;
import com.example.auth.Repository.UserRepository;
import com.example.auth.Security.TokenInfo;
import com.example.auth.Security.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    public AuthService(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
                       RedisRepository redisRepository) {

        this.authenticationManagerBuilder=authenticationManagerBuilder;
        this.tokenProvider=tokenProvider;
        this.redisRepository=redisRepository;
    }

    public TokenInfo login(LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("authentication = " + authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        TokenInfo jwt = tokenProvider.createToken(authentication);

        return jwt;
    }

    public void saveRedis(String refreshToken, String username){
        RefreshToken refreshToken1 = new RefreshToken(refreshToken, username);
        redisRepository.save(refreshToken1);
    }

    public RefreshToken findRefershToken(String username){
       return redisRepository.findRefreshTokenByUsername(username);
    }
    public void validateRefreshToken(RefreshToken refreshToken){

    }
}