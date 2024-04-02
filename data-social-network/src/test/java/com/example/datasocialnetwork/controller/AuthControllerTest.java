package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLogin() {
        LoginRequest loginDTO = new LoginRequest();
        // Set loginDTO properties
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(userService.loginUser(loginDTO)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = authController.login(loginDTO);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).loginUser(loginDTO);
    }

    @Test
    public void testComfirmOTPLogin() {
        ComfirmOTPRequest otpComfirmDTO = new ComfirmOTPRequest();
        // Set loginDTO properties
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(userService.comfirmOTPLogin(otpComfirmDTO)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = authController.comfirmOTPLogin(otpComfirmDTO);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).comfirmOTPLogin(otpComfirmDTO);
    }

    @Test
    public void testRegister_InvalidUser() {
        // Tạo một đối tượng UserDTO không hợp lệ (ví dụ: email rỗng)
        RegisterUserRequest userDTO = new RegisterUserRequest();
        userDTO.setEmail("");
        userDTO.setPassword("password");
        userDTO.setUserName("testUser");

        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.register(userDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testRegister_Success() {
        // Tạo một đối tượng UserDTO hợp lệ
        RegisterUserRequest userDTO = new RegisterUserRequest();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        userDTO.setUserName("testUser");
        when(bindingResult.hasErrors()).thenReturn(false);

        when(userService.createUser(any(RegisterUserRequest.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = authController.register(userDTO, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testForgotPassword() {
        // Set loginDTO properties
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(userService.forgotPassword("test")).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = authController.forgotPassword("test");
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).forgotPassword("test");
    }
}
