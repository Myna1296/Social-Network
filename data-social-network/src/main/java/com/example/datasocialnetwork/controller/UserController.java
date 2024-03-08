package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user-info/{email}")
    public UserInfoResponse getUserInfo( @PathVariable("email") String email){
          return userService.findByEmail(email);
    }

    @PostMapping("/update-image")
    public ResponseEntity<?> updateImage(@RequestBody UserInfo user){
        return userService.updateImageUser(user);
    }
}
