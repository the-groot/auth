package com.example.auth.Controller;

import com.example.auth.Dto.LoginDto;
import com.example.auth.Dto.TokenDto;
import com.example.auth.Entity.RefreshToken;
import com.example.auth.Security.JwtFilter;
import com.example.auth.Security.TokenInfo;
import com.example.auth.Security.TokenProvider;
import com.example.auth.Service.AuthService;
import com.example.auth.Util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AuthService authService;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                          AuthService authService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authService=authService;

    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfo> authorize(@Valid @RequestBody LoginDto loginDto) {
        TokenInfo jwt = authService.login(loginDto);
        authService.saveRedis(jwt.getRefreshToken(), jwt.getUsername());


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        Optional<String> currentUsername = SecurityUtil.getCurrentUsername();
        System.out.println("currentUsername = " + currentUsername);

        return new ResponseEntity<>(jwt, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String reissueAccessToken(@RequestHeader HttpHeaders headers){

        String refreshtoken = authService.reissueRefreshToken(headers.getFirst("refreshtoken"));
        return refreshtoken;
    }


    @PostMapping("/validate")
    public boolean validateAccessToken(@RequestHeader String authorization){
        String accessToken = authorization.substring(7);
        return tokenProvider.validateToken(accessToken);
    }


}

