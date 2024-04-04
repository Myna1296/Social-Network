package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.entity.LikeStatus;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(null);
        when(likeRepository.save(any(LikeStatus.class))).thenReturn(new LikeStatus());


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(new LikeStatus());


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.addLike(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(new LikeStatus());
        doNothing().when(likeRepository).delete(any(LikeStatus.class));


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(new Status());
        when(likeRepository.findLikesByStatusIdAndUserId(1L, 1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = likeServices.deleteLike(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
