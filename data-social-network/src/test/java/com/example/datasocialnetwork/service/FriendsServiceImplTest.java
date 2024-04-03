package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.response.*;
import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FriendsServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendShipRepository friendShipRepository;


    @InjectMocks
    private FriendsServiceImpl friendsService;

    @BeforeEach
    public void setUp(){ }

    @Test
    public void testGetUsersNotAcceptedRequests_Success(){
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
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        friendShip.setUserSender(userSender);
        friendShip.setUserReceiver(userReceiver);
        friendShip.setAccepted(false);
        friendShip.setId(1L);
        friendShip.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countUsersNotAcceptedRequestsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getUsersNotAcceptedRequests(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetUsersNotAcceptedRequests_UserNotFound(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getUsersNotAcceptedRequests(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testGetUsersNotAcceptedRequests_Empty(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);
        // Mock data for Comment
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        friendShip.setUserSender(userReceiver);
        friendShip.setUserReceiver(userSender);
        friendShip.setAccepted(false);
        friendShip.setId(1L);
        friendShip.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                friendRequestDTO.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countUsersNotAcceptedRequestsByUserId(friendRequestDTO.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getUsersNotAcceptedRequests(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetNotAcceptedRequestsToUser_Success(){
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
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        friendShip.setUserSender(userReceiver);
        friendShip.setUserReceiver(userSender);
        friendShip.setAccepted(false);
        friendShip.setId(1L);
        friendShip.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countNotAcceptedRequestsToUserByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getNotAcceptedRequestsToUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetNotAcceptedRequestsToUser_UserNotFound(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getNotAcceptedRequestsToUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testGetNotAcceptedRequestsToUser_Empty(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);
        // Mock data for Comment
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        friendShip.setUserSender(userSender);
        friendShip.setUserReceiver(userReceiver);
        friendShip.setAccepted(false);
        friendShip.setId(1L);
        friendShip.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                friendRequestDTO.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countNotAcceptedRequestsToUserByUserId(friendRequestDTO.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getNotAcceptedRequestsToUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetFriendsOfUser_Success(){
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
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();

        FriendShip friendShip1 = new FriendShip();
        friendShip1.setUserSender(userReceiver);
        friendShip1.setUserReceiver(userSender);
        friendShip1.setAccepted(false);
        friendShip1.setId(1L);
        friendShip1.setCreatedDate(LocalDateTime.now());

        FriendShip friendShip2 = new FriendShip();
        friendShip2.setUserSender(userSender);
        friendShip2.setUserReceiver(userReceiver);
        friendShip2.setAccepted(false);
        friendShip2.setId(1L);
        friendShip2.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip1);
        friendShipList.add(friendShip2);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getFriendsOfUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testGetFriendsOfUser_UserNotFound(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getFriendsOfUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_010, response.getMessage());
    }

    @Test
    public void testGetFriendsOfUser_Empty(){
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

        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(1L);
        friendRequestDTO.setPage(1);
        // Mock data for Comment
        User userSender = new User();
        userSender.setUserName("test1");
        userSender.setId(1L);

        User userReceiver = new User();
        userReceiver.setUserName("test2");
        userReceiver.setId(2L);

        List<FriendShip> friendShipList = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        friendShip.setUserSender(userReceiver);
        friendShip.setUserReceiver(userSender);
        friendShip.setAccepted(false);
        friendShip.setId(1L);
        friendShip.setCreatedDate(LocalDateTime.now());
        friendShipList.add(friendShip);
        Pageable pageable = mock(Pageable.class);

        Page<FriendShip> friendShipPage = new PageImpl<>(friendShipList, pageable, friendShipList.size());
        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, Constants.LIMIT)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<FriendResponse> responseEntity = friendsService.getFriendsOfUser(1L);
        FriendResponse response =  responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

//    @Test
//    public void testCheckFriendShip_Success(){
//        // Mock user details
//        User user = new User();
//        user.setUserName("abc");
//        user.setId(1L);
//        UserAuthDetails authUserDetails = new UserAuthDetails(user);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//        Authentication authentication = mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//
//        // Mock repository methods
//        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);
//
//        when(userRepository.findOneById(2L)).thenReturn(user);
//
//        when(friendShipRepository.checkFriendshipExists(user, user)).thenReturn(true);
//        // Call the method
//        ResponseEntity<CheckFriendShipResponse> responseEntity = friendsService.checkFriendship(2L);
//        CheckFriendShipResponse response =  responseEntity.getBody();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(Constants.CODE_OK, response.getCode());
//    }
//
//    @Test
//    public void testCheckFriendShip_UserNotFound1(){
//        // Mock user details
//        User user = new User();
//        user.setUserName("abc");
//        user.setId(1L);
//        UserAuthDetails authUserDetails = new UserAuthDetails(user);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//        Authentication authentication = mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//
//        // Mock repository methods
//        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
//        // Call the method
//        ResponseEntity<CheckFriendShipResponse> responseEntity = friendsService.checkFriendship(2L);
//        CheckFriendShipResponse response =  responseEntity.getBody();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(Constants.CODE_ERROR, response.getCode());
//        assertEquals(Constants.MESS_010, response.getMessage());
//    }
//
//    @Test
//    public void testCheckFriendShip_UserNotFound2(){
//        // Mock user details
//        User user = new User();
//        user.setUserName("abc");
//        user.setId(1L);
//        UserAuthDetails authUserDetails = new UserAuthDetails(user);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//        Authentication authentication = mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//
//        // Mock repository methods
//        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);
//
//        when(userRepository.findOneById(2L)).thenReturn(null);
//
//        // Call the method
//        ResponseEntity<CheckFriendShipResponse> responseEntity = friendsService.checkFriendship(2L);
//        CheckFriendShipResponse response =  responseEntity.getBody();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(Constants.CODE_ERROR, response.getCode());
//        assertEquals("Friend does not exist", response.getMessage());
//    }
//
//    @Test
//    public void testCheckFriendShip_Faile(){
//        // Mock user details
//        User user = new User();
//        user.setUserName("abc");
//        user.setId(1L);
//        UserAuthDetails authUserDetails = new UserAuthDetails(user);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//        Authentication authentication = mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//
//        // Mock repository methods
//        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);
//        // Call the method
//        ResponseEntity<CheckFriendShipResponse> responseEntity = friendsService.checkFriendship(1L);
//        CheckFriendShipResponse response = responseEntity.getBody();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(Constants.CODE_ERROR, response.getCode());
//        assertEquals("Do not check friends ships with yourself", response.getMessage());
//    }

    @Test
    public void testAddFriendShip_Success(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend)).thenReturn(false);
        when(friendShipRepository.checkFriendshipRequest(user, userFriend)).thenReturn(false);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testAddFriendShip_UserNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    public void testAddFriendShip_Faile(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(1L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Can't send friend requests to myself", response.getMessage());
    }

    @Test
    public void testAddFriendShip_FriendNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(null);

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend does not exist", response.getMessage());
    }

    @Test
    public void testAddFriendShip_AlreadyFriends(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend)).thenReturn(true);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Already friends", response.getMessage());
    }

    @Test
    public void testAddFriendShip_RequestFriends(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend)).thenReturn(false);
        when(friendShipRepository.checkFriendshipRequest(user, userFriend)).thenReturn(true );
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend request already exists", response.getMessage());
    }

    @Test
    public void testDeleteFriendShip_Success(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testDeleteFriendShip_UserNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    public void testDeleteFriendShip_Faile(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(1L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("This friend request does not exist", response.getMessage());
    }

    @Test
    public void testDeleteFriendShip_FriendNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(null);

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend does not exist", response.getMessage());
    }

    @Test
    public void testDeleteFriendShip_NotRequestFriendShip(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend request does not exist", response.getMessage());
    }

    @Test
    public void testAccepteFriendShip_Success(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, response.getCode());
    }

    @Test
    public void testAccepteFriendShip_UserNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("User does not exist", response.getMessage());
    }

    @Test
    public void testAccepteFriendShip_Faile(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(1L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("This friend request does not exist", response.getMessage());
    }

    @Test
    public void testAccepteFriendShip_FriendNotFound(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();
        FriendShip friendShip = new FriendShip();
        list.add(friendShip);

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(null);

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend does not exist", response.getMessage());
    }

    @Test
    public void testAccepteFriendShip_NotRequestFriendShip(){
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

        User userFriend = new User();
        userFriend.setUserName("abc");
        userFriend.setId(2L);

        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setAccepte(true);
        friendShipRequestDTO.setId(2L);

        List<FriendShip> list = new ArrayList<>();

        // Mock repository methods
        when(userRepository.findOneByUserName(user.getUserName())).thenReturn(user);

        when(userRepository.findOneById(2L)).thenReturn(userFriend);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(friendShipRequestDTO);
        ResponseOk response =  (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals("Friend request does not exist", response.getMessage());
    }
}
