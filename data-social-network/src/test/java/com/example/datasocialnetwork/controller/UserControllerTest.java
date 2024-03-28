package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.PasswordChangeDTO;
import com.example.datasocialnetwork.dto.request.SearchUserRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private BindingResult bindingResult;

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

    @Test
    public void testUpdateImage() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);
        when(userService.updateImageUser(new UserInfo())).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = userController.updateImage(new UserInfo());
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updateImageUser(new UserInfo());
    }

    @Test
    public void testUpdateInfo() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);
        when(userService.updateProfileUser(new UserInfo())).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = userController.updateInfo(new UserInfo());
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updateProfileUser(new UserInfo());
    }

    @Test
    public void testUpdatePassword_InvalidUser() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setPassword("123445");
        passwordChangeDTO.setOldPassword("1233");
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.updatePassword(passwordChangeDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdatePassword_OK() {
        PasswordChangeDTO passwordChangeDTO = new PasswordChangeDTO();
        passwordChangeDTO.setPassword("1234455");
        passwordChangeDTO.setOldPassword("12335555");
        when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);

        when(userService.updatePassword(passwordChangeDTO)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = userController.updatePassword(passwordChangeDTO, bindingResult);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updatePassword(passwordChangeDTO);
    }

    @Test
    public void testSearchUserByUsername() {
        ResponseEntity<SearchResponse> expectedResponse = new ResponseEntity<>(new SearchResponse(), HttpStatus.OK);
        when(userService.searchUserByUserName(new SearchUserRequestDTO())).thenAnswer(invocation -> ResponseEntity.ok(new SearchResponse()));
        ResponseEntity<SearchResponse> actualResponse = userController.searchUserByUsername(new SearchUserRequestDTO());
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).searchUserByUserName(new SearchUserRequestDTO());
    }
}
