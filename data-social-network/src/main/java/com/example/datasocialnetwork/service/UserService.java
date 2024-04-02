package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseEntity<?> createUser(RegisterUserRequest userDto);
    ResponseEntity<?> getProfileUser();
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    ResponseEntity<?> loginUser(LoginRequest userLogin);
    ResponseEntity<?> comfirmOTPLogin(ComfirmOTPRequest otpComfirm);
    UserInfoResponse findByEmail(String email);
    UserInfoResponse findById(Long id);
    ResponseEntity<?> updateImageUser(MultipartFile file);
    ResponseEntity<?> updateProfileUser(UserInfo userInfo);
    ResponseEntity<SearchResponse> searchUserByUserName(SearchUserRequestDTO sreachUserRequestDTO);
    ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO);
    ResponseEntity<?> forgotPassword(String email);
}
