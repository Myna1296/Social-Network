package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUserInfo() {
        UserInfoResponse expectedResponse = new UserInfoResponse();
        when(userService.findByEmail("test")).thenReturn(new UserInfoResponse());
        UserInfoResponse actualResponse = userController.getUserInfo("test");
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).findByEmail("test");
    }

    @Test
    public void testGetProfileUserInfoById() {
        UserInfoResponse expectedResponse = new UserInfoResponse();
        when(userService.findById(1L)).thenReturn(new UserInfoResponse());
        UserInfoResponse actualResponse = userController.getProfileUserInfoById(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).findById(1L);
    }
}
