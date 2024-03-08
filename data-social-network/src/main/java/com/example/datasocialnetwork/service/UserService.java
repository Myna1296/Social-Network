package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.LoginDTO;
import com.example.datasocialnetwork.dto.request.OTPComfirmDTO;
import com.example.datasocialnetwork.dto.request.UserDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(UserDTO userDto);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    ResponseEntity<?> loginUser(LoginDTO userLogin);
    ResponseEntity<?> comfirmOTPLogin(OTPComfirmDTO otpComfirm);
    UserInfoResponse findByEmail(String email);
    ResponseEntity<?> updateImageUser(UserInfo userInfo);
}
