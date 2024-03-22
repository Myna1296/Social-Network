package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> createUser(UserDTO userDto);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    ResponseEntity<?> loginUser(LoginDTO userLogin);
    ResponseEntity<?> comfirmOTPLogin(OTPComfirmDTO otpComfirm);
    UserInfoResponse findByEmail(String email);
    UserInfoResponse findById(Long id);
    ResponseEntity<?> updateImageUser(UserInfo userInfo);
    ResponseEntity<?> updateProfileUser(UserInfo userInfo);
    ResponseEntity<SearchResponse> searchUserByUserName(SearchUserRequestDTO sreachUserRequestDTO);
    ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO);
    ResponseEntity<?> forgotPassword(String email);
}
