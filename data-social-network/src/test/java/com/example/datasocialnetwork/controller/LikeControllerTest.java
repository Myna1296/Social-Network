package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.service.impl.LikeServicesImpl;
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
public class LikeControllerTest {

    @Mock
    private LikeServicesImpl likeServices;

    @InjectMocks
    private LikeController likeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddLike() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(likeServices.addLike(1L)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = likeController.addLike(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(likeServices, times(1)).addLike(1L);
    }

    @Test
    public void testDeleteLike() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(likeServices.deleteLike(1L)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = likeController.deleteLike(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(likeServices, times(1)).deleteLike(1L);
    }
}
