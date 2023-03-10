package com.example.auth.Service;

import com.example.auth.Dto.LoginDto;
import com.example.auth.Entity.RefreshToken;
import com.example.auth.Repository.RedisRepository;
import com.example.auth.Vo.DefaultResponse;
import com.example.auth.Vo.ResponseMessage;
import com.example.auth.Vo.StatusCode;
import com.example.auth.Vo.TokenInfo;
import com.example.auth.Security.TokenProvider;
import com.example.auth.Util.SecurityUtil;
import org.aspectj.bridge.Message;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisRepository redisRepository;

    public AuthService(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
                       RedisRepository redisRepository) {

        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
        this.redisRepository = redisRepository;
    }

    public TokenInfo login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        System.out.println("authentication = " + authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        TokenInfo jwt = tokenProvider.createToken(authentication);
        RefreshToken refreshTokenInRedis = findRefreshToken(loginDto.getUsername());

        if (Objects.isNull(refreshTokenInRedis)) {    //redis??? refreshtoken ????????? ???????????????
            RefreshToken redisRefreshToken = new RefreshToken(jwt.getRefreshToken(), loginDto.getUsername());
            redisRepository.save(redisRefreshToken);
        } else {   //????????? ???????????????x
            jwt.setRefreshToken(null);
        }

        return jwt;
    }


    public RefreshToken findRefreshToken(String username) {
        return redisRepository.findRefreshTokenByUsername(username);
    }

    public boolean validateRefreshToken(RefreshToken refreshTokenInRedis, String refreshTokenInHeaders) {
        System.out.println("refreshTokenInRedis = " + refreshTokenInRedis);
        System.out.println("refreshTokenInHeaders = " + refreshTokenInHeaders);

        if (Objects.isNull(refreshTokenInRedis)) {    //refreshtoken??? ??????????????? ????????? ??????
            return false;
        } else {   //refreshtoken??? ????????????
            System.out.println("refreshTokenInRedis.getRefreshToken() = " + refreshTokenInRedis.getRefreshToken());
            if (!refreshTokenInRedis.getRefreshToken().equals(refreshTokenInHeaders)) {
                System.out.println("????????? ?????? ????????? ???????????? ????????????.");
                return false;
            } else {
                return true;
            }

        }


    }

    public DefaultResponse reissueRefreshToken(String refreshTokenInHeaders) {
        String username = SecurityUtil.getCurrentUsername().get();
        RefreshToken refreshTokenInRedis = findRefreshToken(username);

        if (Objects.isNull(refreshTokenInRedis)) {    //refreshtoken??? ??????????????? ????????? ??????
            return new DefaultResponse(StatusCode.RE_LOGIN, ResponseMessage.LOGIN_AGAIN, null);
        } else {   //refreshtoken??? ????????????
            System.out.println("refreshTokenInRedis.getRefreshToken() = " + refreshTokenInRedis.getRefreshToken());
            if (!refreshTokenInRedis.getRefreshToken().equals(refreshTokenInHeaders)) {   //?????? ????????? ???????????? ?????????
                System.out.println("????????? ?????? ????????? ???????????? ????????????.");
                return new DefaultResponse(StatusCode.TOKEN_INVALID, ResponseMessage.TOKEN_INVALID, null);
            } else {
                final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String accessToken = tokenProvider.createAccessToken(authentication);
                return new DefaultResponse(StatusCode.OK, ResponseMessage.TOKEN_REISSUE, accessToken);
            }
        }

     /*   System.out.println("refreshTokenInRedis" + refreshTokenInRedis);
        System.out.println("refreshTokenInHeaders = " + refreshTokenInHeaders);

        if(validateRefreshToken(refreshTokenInRedis,refreshTokenInHeaders)){
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accessToken = tokenProvider.createAccessToken(authentication);
            return accessToken;
        }
        else{
            System.out.println("refresh token??? ???????????? ????????????");
            return null;
        }*/


    }
}