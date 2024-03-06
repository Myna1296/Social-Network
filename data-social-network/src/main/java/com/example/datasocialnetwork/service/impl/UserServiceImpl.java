package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Gender;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.dto.request.LoginDTO;
import com.example.datasocialnetwork.dto.request.UserDTO;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.datasocialnetwork.common.Constants;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailService mailService;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
        this.passwordEncoder = new StandardPasswordEncoder();
    }

    /*
    Handles user registration
    Create by NgaPLT 2024/03/06
    <param>UserDTO</param>
    <returns></returns>
     */
    @Override
    @Transactional
    public ResponseEntity<?> createUser(UserDTO userDto) {
        //Check if user name and email exist or not
        User user = convertUserDtoToUser(userDto);
        String result = validateInfo(user);
        if(result != null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        userRepository.save(user);
        ResponseOk response = new ResponseOk(Constants.CODE_OK,Constants.MESS_003);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<?> loginUser(LoginDTO userLogin) {
        User user = userRepository.findOneByEmail(userLogin.getEmail());
        if (user == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_004);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if ( !user.getPassword().equals(passwordEncoder.encode(userLogin.getPassword()))){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_005);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        boolean sednMail = mailService.sendCode(user.getEmail(), user.getUserName(), SendCodeType.LOGIN);
        if( sednMail){
            ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseOk response = new ResponseOk(Constants.CODE_OK, Constants.MESS_006);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String validateInfo(User user){
        if(existsByUsername(user.getUserName())) {
            return Constants.MESS_001;
        }
        if(existsByEmail(user.getEmail())){
            return Constants.MESS_002;
        }
        return null;
    }

    private User convertUserDtoToUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setUserName(userDTO.getUserName());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setSex(Gender.getGenderByName(userDTO.getSex()));
        user.setImage(userDTO.getImage());
        user.setBirthday(userDTO.getBirthday());
        user.setJob(userDTO.getJob());
        if(userDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return user;
    }
}
