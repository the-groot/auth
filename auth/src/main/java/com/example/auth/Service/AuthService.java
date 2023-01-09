package com.example.auth.Service;

import com.example.auth.Dto.UserDto;
import com.example.auth.Entity.User;
import com.example.auth.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createAccessToken(){

    }
    public void createRefreshToken(){

    }
    public void loginCheck(UserDto userDto){
        System.out.println(userDto.getUsername());
        User user = userRepository.findOneByUsername(userDto.getUsername()).orElse(null);
        System.out.println("user = " + user);
        if(user==null){
            //case1 회원정보 존재x
            System.out.println("아이디 틀림");
        }
        else{           //case2 회원정보 존재
            //case1-1 비밀번호 맞음
            if(userDto.getPassword().equals(user.getPassword())){
                System.out.println("비밀번호 맞음");
            }

            //case1-2 비밀번호 틀림
            else{
                System.out.println("비밀번호 틀림");
            }




        }










    }
}
