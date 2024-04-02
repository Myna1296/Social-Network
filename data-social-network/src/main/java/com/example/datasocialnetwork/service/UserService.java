package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(RegisterUserRequest userDto);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    ResponseEntity<?> loginUser(LoginRequest userLogin);
    ResponseEntity<?> comfirmOTPLogin(ComfirmOTPRequest otpComfirm);
    UserInfoResponse findByEmail(String email);
    UserInfoResponse findById(Long id);
    ResponseEntity<?> updateImageUser(UserInfo userInfo);
    ResponseEntity<?> updateProfileUser(UserInfo userInfo);
    ResponseEntity<SearchResponse> searchUserByUserName(SearchUserRequestDTO sreachUserRequestDTO);
    ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO);
    ResponseEntity<?> forgotPassword(String email);
}
