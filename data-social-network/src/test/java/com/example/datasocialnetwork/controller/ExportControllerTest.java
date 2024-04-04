package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
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
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(exportServices.exportFile()).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = exportController.exportFile();
        assertEquals(expectedResponse, actualResponse);
        verify(exportServices, times(1)).exportFile();
    }
}
