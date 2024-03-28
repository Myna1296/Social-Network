package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.response.ResponseOk;
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
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class StatusControllerTest {

    @Mock
    private StatusServiceImpl statusService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveStatus_Success() {
        // Tạo một đối tượng UserDTO hợp lệ
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setContent("test");
        statusDTO.setTitle("test         ");

        when(bindingResult.hasErrors()).thenReturn(false);

        when(statusService.addNewStatus(any(StatusDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = statusController.saveStatus(statusDTO, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSaveStatus_Faile() {
        // Tạo một đối tượng UserDTO hợp lệ
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setContent("");
        statusDTO.setTitle("");

        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> responseEntity = statusController.saveStatus(statusDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateStatus_Success() {
        // Tạo một đối tượng UserDTO hợp lệ
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setContent("test");
        statusDTO.setTitle("test         ");

        when(bindingResult.hasErrors()).thenReturn(false);

        when(statusService.updateStatus(any(StatusDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = statusController.updateStatus(statusDTO, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testUpdateStatus_Faile() {
        // Tạo một đối tượng UserDTO hợp lệ
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setContent("");
        statusDTO.setTitle("");

        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> responseEntity = statusController.updateStatus(statusDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteStatus() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(statusService.deleteStatus(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = statusController.deleteStatus(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).deleteStatus(1L);
    }

    @Test
    public void testGetStatusFriendUser() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(statusService.getStatusFriendUser(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = statusController.getStatusFriendUser(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).getStatusFriendUser(1L);
    }

    @Test
    public void testGetPostByUserId() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(statusService.getStatusByUserId(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = statusController.getPostByUserId(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).getStatusByUserId(1L);
    }

    @Test
    public void testSearch() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(statusService.searchStatus(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = statusController.search(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(statusService, times(1)).searchStatus(1L);
    }
}
