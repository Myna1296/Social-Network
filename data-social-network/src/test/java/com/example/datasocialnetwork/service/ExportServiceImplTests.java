package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExportServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendShipRepository friendShipRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private ExportServicesImpl exportServices;

    @BeforeEach
    public void setUp(){ }

    @Test
    public void testExportFile_Success() {
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

        Long number = Long.parseLong("10");

        // Mock repository methods
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(friendShipRepository.countAcceptedFriendshipsAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);
        when(statusRepository.countStatusTAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);
        when(commentRepository.countCommentsByUserIdAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);
        when(commentRepository.countCommentsByStatusUserIdAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);
        when(likeRepository.countLikesByUserIdAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);
        when(likeRepository.countLikesByStatusUserIdAndCreatedDateAfter(anyLong(), any(LocalDateTime.class))).thenReturn(number);


        // Call the method
        ResponseEntity<?> responseEntity = exportServices.exportFile();
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        assertEquals(expectedResponse.getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    public void testExportFile_UserNotFound() {
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
        when(userRepository.findOneById(1L)).thenReturn(null);


        // Call the method
        ResponseEntity<?> responseEntity = exportServices.exportFile();
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testExportFile_Exception() {
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
        when(userRepository.findOneById(1L)).thenThrow(new RuntimeException("User not found"));


        // Call the method
        ResponseEntity<?> responseEntity = exportServices.exportFile();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
