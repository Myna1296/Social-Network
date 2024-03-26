package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.StatusAllResponse;
import com.example.datasocialnetwork.dto.response.StatusInfoResponse;
import com.example.datasocialnetwork.entity.Comment;
import com.example.datasocialnetwork.entity.LikeStatus;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.CommentRepository;
import com.example.datasocialnetwork.repository.LikeRepository;
import com.example.datasocialnetwork.repository.StatusRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.impl.LikeServicesImpl;
import com.example.datasocialnetwork.service.impl.StatusServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatusServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    @BeforeEach
    public void setUp(){ }

    @Test
    public void testGetStatusByUserId_Success(){
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

        // Mock data for Comment
        List<Status> statusList = new ArrayList<>();
        Status status = new Status();
        status.setUser(user);
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setStatusText("test");
        status.setCommentCount(1);
        status.setLikeCount(1);
        status.setCreatedDate(LocalDateTime.now());
        status.setId(1L);
        statusList.add(status);
        Pageable pageable = mock(Pageable.class);

        Page<Status> statusPage = new PageImpl<>(statusList, pageable, statusList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(statusPage);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.getStatusByUserId(1L);
        StatusAllResponse response = (StatusAllResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetStatusByUserId_UserNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.getStatusByUserId(1L);
        StatusAllResponse response = (StatusAllResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testGetStatusFriendUser_Success(){
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

        // Mock data for Comment
        List<Status> statusList = new ArrayList<>();
        Status status = new Status();
        status.setUser(user);
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setStatusText("test");
        status.setCommentCount(1);
        status.setLikeCount(1);
        status.setCreatedDate(LocalDateTime.now());
        status.setId(1L);
        statusList.add(status);
        Pageable pageable = mock(Pageable.class);

        Page<Status> statusPage = new PageImpl<>(statusList, pageable, statusList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusOfAllFriends(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(statusPage);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.getStatusFriendUser(1L);
        StatusAllResponse response = (StatusAllResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetStatusFriendUser_UserNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.getStatusFriendUser(1L);
        StatusAllResponse response = (StatusAllResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testSearchStatus_Success(){
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


        Status status = new Status();
        status.setUser(user);
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setStatusText("test");
        status.setCommentCount(1);
        status.setLikeCount(1);
        status.setCreatedDate(LocalDateTime.now());
        status.setId(1L);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(status);
        when(likeRepository.countByStatusIdAndUserId(1L,1L)).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.searchStatus(1L);
        StatusInfoResponse response = (StatusInfoResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testSearchStatus_UserNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.searchStatus(1L);
        StatusInfoResponse response = (StatusInfoResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testSearchStatus_StatusNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.searchStatus(1L);
        StatusInfoResponse response = (StatusInfoResponse) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Status does not exist", response.getMessage());
    }

    @Test
    public void testDeleteStatus_Success(){
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


        Status status = new Status();
        status.setUser(user);
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setStatusText("test");
        status.setCommentCount(1);
        status.setLikeCount(1);
        status.setCreatedDate(LocalDateTime.now());
        status.setId(1L);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(status);
        doNothing().when(likeRepository).deleteLikeStatusByStatusId(1L);
        doNothing().when(commentRepository).deleteCommentByStatusId(1L);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.deleteStatus(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testDeleteStatus_UserNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.deleteStatus(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testDeleteStatus_StatusNotFound(){
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
        ResponseEntity<?> responseEntity = statusService.deleteStatus(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Status does not exist", response.getMessage());
    }

    @Test
    public void testDeleteStatus_StatusNotOfUser(){
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

        User userStatus = new User();
        userStatus.setUserName("abc");
        userStatus.setId(2L);
        Status status = new Status();
        status.setUser(userStatus);
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setStatusText("test");
        status.setCommentCount(1);
        status.setLikeCount(1);
        status.setCreatedDate(LocalDateTime.now());
        status.setId(1L);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(status);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.deleteStatus(1L);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Cannot delete other people's status", response.getMessage());
    }

    @Test
    public void testAddNewStatus_Success(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(user);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.addNewStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testAddNewStatus_UsernotFound1(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.addNewStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testAddNewStatus_UsernotFound2(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.addNewStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testAddNewStatus_AuthFaile(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(2L);
        status.setUserName("test");

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(2L)).thenReturn(user);

        // Call the method
        ResponseEntity<?> responseEntity = statusService.addNewStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Authentication information does not match", response.getMessage());
    }

    @Test
    public void testUpdateStatus_Success(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");
        status.setId(1L);

        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(user);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(st);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testUpdateStatus_UsernotFound1(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");
        status.setId(1L);

        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(user);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testUpdateStatus_UsernotFound2(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");
        status.setId(1L);

        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(user);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testUpdateStatus_AuthFaile1(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(2L);
        status.setUserName("test");
        status.setId(1L);

        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(user);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(2L)).thenReturn(user);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Authentication information does not match", response.getMessage());
    }

    @Test
    public void testUpdateStatus_StatusNotOfUser(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");
        status.setId(1L);

        User userStatus = new User();
        userStatus.setId(2L);
        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(userStatus);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(st);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Cannot update other people's status", response.getMessage());
    }

    @Test
    public void testUpdateStatus_StatusNotFound(){
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


        StatusDTO status = new StatusDTO();
        status.setStatusImage("test.jpg");
        status.setTitle("test");
        status.setContent("test");
        status.setUserId(1L);
        status.setUserName("test");
        status.setId(1L);

        Status st = new Status();
        st.setId(1L);
        st.setStatusText("");
        st.setUser(user);

        // Mock repository methods
        when(userRepository.findOneByUserName(anyString())).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(user);
        when(statusRepository.findStatusById(1L)).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = statusService.updateStatus(status);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Status does not exist", response.getMessage());
    }

}
