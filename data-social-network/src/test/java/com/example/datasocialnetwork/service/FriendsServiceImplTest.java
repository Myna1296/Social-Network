package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0,5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countUsersNotAcceptedRequestsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getUserSentFriendRequest(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, 5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countUsersNotAcceptedRequestsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getUserSentFriendRequest(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, 5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countNotAcceptedRequestsToUserByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getWaitingUserToAccept(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, 5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countNotAcceptedRequestsToUserByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getWaitingUserToAccept(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, 5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getAccepteFriendRequest(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        when(userRepository.findOneById(1L)).thenReturn(user);

        when(friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of( 0, 5)
        )).thenReturn(friendShipPage);

        when(friendShipRepository.countAcceptedFriendshipsByUserId(user.getId())).thenReturn(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.getAccepteFriendRequest(1,5);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckFriendShip_Success(){
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

        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(friend).when(userRepository).findOneById(2L);

        when(friendShipRepository.checkFriendshipExists(user, friend)).thenReturn(true);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.checkFriendship(2L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckFriendShip_UserNotFound1(){
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
        when(userRepository.findOneById(2L)).thenReturn(null);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.checkFriendship(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testCheckFriendShip_UserNotFound2(){
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

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.checkFriendship(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


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

        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(friend).when(userRepository).findOneById(2L);


        when(friendShipRepository.checkFriendshipExists(user, friend)).thenReturn(false);
        when(friendShipRepository.checkFriendshipRequest(user, friend)).thenReturn(false);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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

        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(null).when(userRepository).findOneById(2L);


        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(1L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(friend).when(userRepository).findOneById(2L);


        when(friendShipRepository.checkFriendshipExists(user, friend)).thenReturn(true);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        User friend = new User();
        friend.setUserName("abc");
        friend.setId(2L);
        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(friend).when(userRepository).findOneById(2L);


        when(friendShipRepository.checkFriendshipExists(user, friend)).thenReturn(false);
        when(friendShipRepository.checkFriendshipRequest(user, friend)).thenReturn(true);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.addFriendRequest(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(userFriend).when(userRepository).findOneById(2L);


        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(null).when(userRepository).findOneById(2L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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
        doReturn(user).when(userRepository).findOneById(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(userFriend).when(userRepository).findOneById(2L);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, true)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.deleteFriendship(friendShipRequestDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(userFriend).when(userRepository).findOneById(2L);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, false)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(2L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
        doReturn(user).when(userRepository).findOneById(1L);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(1l);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(null).when(userRepository).findOneById(2L);

        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
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

        doReturn(user).when(userRepository).findOneById(1L);
        doReturn(userFriend).when(userRepository).findOneById(2L);

        when(friendShipRepository.checkFriendshipExists(user, userFriend, false)).thenReturn(list);
        // Call the method
        ResponseEntity<?> responseEntity = friendsService.accepteFriendShip(2L);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
