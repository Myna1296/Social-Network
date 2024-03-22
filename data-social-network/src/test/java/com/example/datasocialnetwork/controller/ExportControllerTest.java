package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.LoginDTO;
import com.example.datasocialnetwork.dto.request.OTPComfirmDTO;
import com.example.datasocialnetwork.dto.request.UserDTO;
import com.example.datasocialnetwork.dto.response.ResponseOk;
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
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class ExportControllerTest {

    @Mock
    private ExportServicesImpl exportServices;

    @InjectMocks
    private ExportController exportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExportFile() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(exportServices.exportFile()).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = exportController.exportFile();
        assertEquals(expectedResponse, actualResponse);
        verify(exportServices, times(1)).exportFile();
    }
}
