package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class FriendControllerTest {

    @Mock
    private FriendsServiceImpl friendsService;

    @InjectMocks
    private FriendsController friendsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAccepteFriendRequest() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.getAccepteFriendRequest(1, 5)).thenAnswer(invocation -> new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.getFriendsOfUser(1, 5);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getAccepteFriendRequest(1, 5);
    }

    @Test
    public void testCheckFriendShip() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.checkFriendship(1L)).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.checkFriendShip(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).checkFriendship(1L);
    }
    @Test
    public void testAddFriendRequest() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.addFriendRequest(1L)).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.addFriendRequest(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).addFriendRequest(1L);
    }
    @Test
    public void testGetUserSentFriendRequest() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.getUserSentFriendRequest(1,5)).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.getUserSentFriendRequest(1,5);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getUserSentFriendRequest(1,5);
    }
    @Test
    public void testGetRequestNotAccepteToUser() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.getWaitingUserToAccept(1,5)).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.getWaitingUserToAccept(1,5);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getWaitingUserToAccept(1,5);
    }
    @Test
    public void testDeleteFriendship() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.deleteFriendship(new FriendShipRequestDTO())).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.deleteFriendship(new FriendShipRequestDTO());
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).deleteFriendship(new FriendShipRequestDTO());
    }
    @Test
    public void testAccepteFriendship() {
        ResponseEntity<?> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(friendsService.accepteFriendShip(1L)).thenAnswer(invocation ->new ResponseEntity<>(HttpStatus.OK));
        ResponseEntity<?> actualResponse = friendsController.accepteFriendship(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).accepteFriendShip(1L);
    }

}
