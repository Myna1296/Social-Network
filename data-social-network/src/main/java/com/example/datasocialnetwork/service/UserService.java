package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<?> createUser(RegisterUserRequest userDto);
    Boolean existsByEmail(String email);
    ResponseEntity<?> loginUser(LoginRequest userLogin);
    ResponseEntity<?> comfirmOTPLogin(ComfirmOTPRequest otpComfirm);
    ResponseEntity<?> findById(Long id);
    ResponseEntity<?> updateImageUser(MultipartFile file);
    ResponseEntity<?> updateProfileUser(UserInfo userInfo);
    ResponseEntity<?> searchUserByUserName(SearchRequest searchRequest);
    ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO);
    ResponseEntity<?> forgotPassword(String email);
}
