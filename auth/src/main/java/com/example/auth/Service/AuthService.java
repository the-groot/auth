package com.example.auth.Service;

import com.example.auth.Dto.LoginDto;
import com.example.auth.Entity.RefreshToken;
import com.example.auth.Repository.RedisRepository;
import com.example.auth.Repository.UserRepository;
import com.example.auth.Security.TokenInfo;
import com.example.auth.Security.TokenProvider;
import com.example.auth.Util.SecurityUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
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
        RefreshToken refreshTokenInRedis = findRefreshToken(username);
        if(Objects.isNull(refreshTokenInRedis)){
            redisRepository.save(refreshToken1);
        }
        else if(!tokenProvider.validateToken(refreshTokenInRedis.getRefreshToken())){
            redisRepository.delete(refreshTokenInRedis);
        }
    }

    public RefreshToken findRefreshToken(String username){
       return redisRepository.findRefreshTokenByUsername(username);
    }
    public boolean validateRefreshToken(RefreshToken refreshTokenInRedis, String refreshTokenInHeaders){
        if(!tokenProvider.validateToken(refreshTokenInRedis.getRefreshToken())){
            System.out.println("Refresh Token이 유효하지 않습니다.");
            return false;
        }
        else if(!refreshTokenInRedis.getRefreshToken().equals(refreshTokenInHeaders)){
            System.out.println("토큰의 유저 정보가 일치하지 않습니다.");
            return false;
        }
        else{
            return true;
        }

    }

    public String reissueRefreshToken(String refreshTokenInHeaders){
        String username = SecurityUtil.getCurrentUsername().get();
        RefreshToken refreshTokenInRedis = findRefreshToken(username);

        System.out.println("refreshTokenInRedis.getRefreshToken() = " + refreshTokenInRedis.getRefreshToken());
        System.out.println("refreshTokenInHeaders = " + refreshTokenInHeaders);

        if(validateRefreshToken(refreshTokenInRedis,refreshTokenInHeaders)){
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accessToken = tokenProvider.createAccessToken(authentication);
            return accessToken;
        }
        else{
            System.out.println("refresh token이 유효하지 않습니다");
            return null;
        }

    }
}