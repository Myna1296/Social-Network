package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.PasswordChangeDTO;
import com.example.datasocialnetwork.dto.request.SearchRequest;
import com.example.datasocialnetwork.dto.request.UserInfo;
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
import static org.mockito.ArgumentMatchers.any;
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
    public void testGetProfileUserInfoById() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userService.findById(1L)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = userController.getProfileUserInfoById(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).findById(1L);
    }

    @Test
    public void testUpdateImage() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userService.updateImageUser(any())).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = userController.updateImage(any());
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updateImageUser(any());
    }

    @Test
    public void testUpdateInfo() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userService.updateProfileUser(new UserInfo())).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
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
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(userService.updatePassword(passwordChangeDTO)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = userController.updatePassword(passwordChangeDTO, bindingResult);
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).updatePassword(passwordChangeDTO);
    }

    @Test
    public void testSearchUserByUsername() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(userService.searchUserByUserName(new SearchRequest())).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = userController.searchUserByUsername(new SearchRequest());
        assertEquals(expectedResponse, actualResponse);
        verify(userService, times(1)).searchUserByUserName(new SearchRequest());
    }
}
