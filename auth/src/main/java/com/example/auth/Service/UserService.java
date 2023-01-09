package com.example.auth.Service;



import com.example.auth.Vo.DefaultResVo;
import com.example.auth.Vo.ResponseMessageVo;
import com.example.auth.Vo.StatusCodeVo;
import com.example.auth.Dto.UserDto;
import com.example.auth.Entity.User;
import com.example.auth.Repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    @Transactional
    public ResponseEntity signup(UserDto userDto){
        int statusCode= StatusCodeVo.OK;
        String responseMesage= ResponseMessageVo.SIGNUP_SUCCESS;


        if (userRepository.findOneByUsername(userDto.getUsername()).orElse(null)!=null){
            statusCode= StatusCodeVo.CONFLICT;
            responseMesage= ResponseMessageVo.SIGNUP_FAIL;
           // throw new DuplicateMemberException("이미 가입되어 있는 유저입니다");
        }

        else{
                User user = User.builder()
                    .username(userDto.getUsername())
                    .password((userDto.getPassword()))    //유저정보 넣고
                    .build();
            userRepository.save(user);
        }

        return new ResponseEntity(DefaultResVo.res(statusCode, responseMesage, userDto), HttpStatus.CONFLICT);
    }


}
