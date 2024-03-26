package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.entity.LikeStatus;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
import com.example.datasocialnetwork.service.impl.LikeServicesImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeServicesImpl likeServices;

    @BeforeEach
    public void setUp(){ }

    @Test
    public void testAddLike_Success() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(null);
        when(likeRepository.save(any(LikeStatus.class))).thenReturn(new LikeStatus());


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
        assertEquals("", response.getMessage());
    }

    @Test
    public void testAddLike_UserNotFound() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_013, response.getMessage());
    }

    @Test
    public void testAddLike_StatusNotFound() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Status does not exist", response.getMessage());
    }

    @Test
    public void testAddLike_Liked() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(new LikeStatus());


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("I've already liked this post, but I can't like it again.", response.getMessage());
    }

    @Test
    public void testDeleteLike_Success() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(new LikeStatus());
        doNothing().when(likeRepository).delete(any(LikeStatus.class));


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
        assertEquals("", response.getMessage());
    }

    @Test
    public void testDeleteLike_UserNotFound() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_013, response.getMessage());
    }

    @Test
    public void testDeleteLike_StatusNotFound() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Status does not exist", response.getMessage());
    }

    @Test
    public void testDeleteLike_LikeNotFound() {
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);


        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Haven't liked the post yet, can't unlike it", response.getMessage());
    }

}
