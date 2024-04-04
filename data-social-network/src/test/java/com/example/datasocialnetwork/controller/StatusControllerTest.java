package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.service.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {

    @Mock
    private StatusServiceImpl statusService;


    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveStatus_Success() {

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "content".getBytes());

        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(statusService.addNewStatus(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> actualResponse = statusController.saveStatus(file, "test", "test");

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testUpdateStatus_Success() {

        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "content".getBytes());

        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

        when(statusService.updateStatus(any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> actualResponse = statusController.updateStatus(file, "test", "test", 1L);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void testDeleteStatus() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(statusService.deleteStatus(1L)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = statusController.deleteStatus(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).deleteStatus(1L);
    }

    @Test
    public void testGetNewsFeed() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(statusService.getNewsFeed(1, 5)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = statusController.getNewsFeed(1,5 );
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).getNewsFeed(1,5 );
    }

    @Test
    public void testGetPostByUserId() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(statusService.getStatusByUserId(new StatusRequest())).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = statusController.getStatusFriendUser(new StatusRequest());
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).getStatusByUserId(new StatusRequest());
    }

    @Test
    public void testSearch() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(statusService.searchStatus(1L)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = statusController.search(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).searchStatus(1L);
    }
}
