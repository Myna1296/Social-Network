package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.request.NewCommentRequest;
import com.example.datasocialnetwork.entity.Comment;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.CommentRepository;
import com.example.datasocialnetwork.repository.StatusRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp(){ }

    @Test
    public void testSearchCommentByStatusId_Success() {

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPageIndex(1);
        commentRequest.setStatusId(1L);
        commentRequest.setPageSize(5);

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        user.setImage("test.jpg");
        user.setUserName("test");

        Status status = new Status();
        status.setId(1L);

        // Mock data for Comment
        List<Comment> commentList = new ArrayList<>();
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setStatus(status);
        comment.setCommentText("test");
        comment.setCreatedDate(LocalDateTime.now());
        comment.setId(1L);
        commentList.add(comment);
        Pageable pageable = mock(Pageable.class);

        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());


        when(statusRepository.findStatusById(commentRequest.getStatusId())).thenReturn(new Status());
        when(commentRepository.findCommentsByStatusId(
                commentRequest.getStatusId(),
                PageRequest.of(commentRequest.getPageIndex() - 1, commentRequest.getPageSize())
        )).thenReturn(commentPage);

        // Call the method
        ResponseEntity<?> responseEntity = commentService.searchCommentByStatusId(commentRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSearchCommentByStatusId_StatusNotFound() {

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setPageIndex(1);
        commentRequest.setStatusId(1L);
        commentRequest.setPageSize(5);

        when(statusRepository.findStatusById(commentRequest.getStatusId())).thenReturn(null);

        // Call the method
        ResponseEntity<?> responseEntity = commentService.searchCommentByStatusId(commentRequest);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void testAddNewCommen_Success() {
        NewCommentRequest commentDTO = new NewCommentRequest();
        commentDTO.setStatusId(1L);
        commentDTO.setContent("test");

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(commentDTO.getStatusId())).thenReturn(new Status());
        // Call the method
        ResponseEntity<?> responseEntity = commentService.addNewCommen(commentDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testAddNewCommen_StatusNotFound() {
        NewCommentRequest commentDTO = new NewCommentRequest();
        commentDTO.setStatusId(1L);
        commentDTO.setContent("test");

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(commentDTO.getStatusId())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = commentService.addNewCommen(commentDTO);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteCommen_Success() {

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        Comment comment = new Comment();
        comment.setUser(user);
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(commentRepository.findOneById(1L)).thenReturn(comment);
        doNothing().when(commentRepository).delete(any(Comment.class));
        // Call the method
        ResponseEntity<?> responseEntity = commentService.deleteCommnet(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteCommen_CommentNotFound() {

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        when(userRepository.findOneById(1L)).thenReturn(user);
        when(commentRepository.findOneById(1L)).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = commentService.deleteCommnet(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteCommen_CommentNotOfUser() {

        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        User userComment = new User();
        userComment.setUserName("abc");
        userComment.setId(2L);
        Comment comment = new Comment();
        comment.setUser(userComment);
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(commentRepository.findOneById(1L)).thenReturn(comment);
        // Call the method
        ResponseEntity<?> responseEntity = commentService.deleteCommnet(1L);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }



}
