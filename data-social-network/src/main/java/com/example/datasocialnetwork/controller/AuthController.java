package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.ErrorResponse;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    /*
    Handles user registration
    Create by NgaPLT 2024/03/06
    <param>UserDTO</param>
    <returns></returns>
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserRequest user, BindingResult bindingResult){
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
        return userService.createUser(user);
    }

    /*
    Handles user login
    Create by NgaPLT 2024/03/06
    <param>LoginRequest</param>
    <returns></returns>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest userLogin){
        return userService.loginUser(userLogin);
    }

    /*
    Handles user login comfirm otp
    Create by NgaPLT 2024/03/06
    <param>ComfirmOTPRequest</param>
    <returns></returns>
    */
    @PostMapping("/comfirm-otp-login")
    public ResponseEntity<?> comfirmOTPLogin(@RequestBody ComfirmOTPRequest otpComfirm){
        return userService.comfirmOTPLogin(otpComfirm);
    }

    /*
    Handles user forgot password
    Create by NgaPLT 2024/03/06
    <param>email</param>
    <returns></returns>
     */
    @PutMapping("/forgot-password/{email}")
    public ResponseEntity<?> forgotPassword(@PathVariable("email")  String email) {
        return userService.forgotPassword(email);
    }

}
