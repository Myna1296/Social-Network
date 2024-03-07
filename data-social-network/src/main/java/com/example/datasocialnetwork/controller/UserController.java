package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user-info/{email}")
    public UserInfoResponse getUserInfo( @PathVariable("email") String email){
          return userService.findByEmail(email);
    }
}
