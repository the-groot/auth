package com.example.auth.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .authorizeRequests()
                .antMatchers("/api/userlist").permitAll()
                .antMatchers("/api/signup").permitAll()
                .antMatchers("/api/hello").permitAll()
                .anyRequest().authenticated();
        return http.build();
    }
}
