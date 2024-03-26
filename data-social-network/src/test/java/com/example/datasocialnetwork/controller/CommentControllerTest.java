package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.CommentDTO;
import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.service.impl.CommentServiceImpl;
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
public class CommentControllerTest {

    @Mock
    private CommentServiceImpl commentService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCommentByStatusId() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(commentService.searchCommentByStatusId(new CommentRequest())).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = commentController.getCommentByStatusId(new CommentRequest());
        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).searchCommentByStatusId(new CommentRequest());
    }

    @Test
    public void testAddNewComment_Success() {
        // Tạo một đối tượng UserDTO hợp lệ
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("test");

        when(bindingResult.hasErrors()).thenReturn(false);

        when(commentService.addNewCommen(any(CommentDTO.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> responseEntity = commentController.addNewComment(commentDTO, bindingResult);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testAddNewComment_Faile() {
        // Tạo một đối tượng UserDTO hợp lệ
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent("");

        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> responseEntity = commentController.addNewComment(commentDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteComment() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(HttpStatus.OK.value(), ""), HttpStatus.OK);
        when(commentService.deleteCommnet(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk(HttpStatus.OK.value(), "")));
        ResponseEntity<?> actualResponse = commentController.deleteComment(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(commentService, times(1)).deleteCommnet(1L);
    }
}
