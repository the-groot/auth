package com.example.auth.Service;

import com.example.auth.Entity.CustomUserDetails;
import com.example.auth.Entity.User;
import com.example.auth.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> oneByUsername = userRepository.findOneByUsername(username);
        if(oneByUsername.isEmpty()){    //못찾았을때
            throw new UsernameNotFoundException(username+"을 찾지 못했습니다");
        }
        else{   //찾았을때
            CustomUserDetails customUserDetails = CustomUserDetails.builder()
                    .username(oneByUsername.get().getUsername())
                    .password(oneByUsername.get().getPassword())
                    .build();

            return customUserDetails;

        }




    }
}
