package org.example.datasocialnetwork.controller;

import org.example.datasocialnetwork.dto.request.UserDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDTO userDTO){


    }

}