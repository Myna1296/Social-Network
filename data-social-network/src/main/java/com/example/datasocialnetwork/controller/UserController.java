package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.PasswordChangeDTO;
import com.example.datasocialnetwork.dto.request.SearchUserRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.ErrorResponse;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user-info/{email}")
    public UserInfoResponse getUserInfo( @PathVariable("email") String email){
          return userService.findByEmail(email);
    }

    @GetMapping("/profile-user/{id}")
    public UserInfoResponse getProfileUserInfoById( @PathVariable("id")  Long id){
        return userService.findById(id);
    }

    @PostMapping("/update-image")
    public ResponseEntity<?> updateImage(@RequestBody UserInfo user){
        return userService.updateImageUser(user);
    }

    @PostMapping("/update-profile-user")
    public ResponseEntity<?> updateInfo(@RequestBody UserInfo userInfo){
        return  userService.updateProfileUser(userInfo);
    }
    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordChangeDTO passwordChangeDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrors(errors);
            errorResponse.setMessage("Invalid data submitted");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return userService.updatePassword(passwordChangeDTO);
    }

    @PostMapping("/search-user")
    public ResponseEntity<SearchResponse> searchUserByUsername(@RequestBody SearchUserRequestDTO sreachUserRequestDTO){
        return userService.searchUserByUserName(sreachUserRequestDTO);
    }
}
