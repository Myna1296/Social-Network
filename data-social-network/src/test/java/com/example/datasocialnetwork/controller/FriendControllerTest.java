package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.dto.response.ResponseOk;
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
    public void testGetFriendsOfUser() {
        ResponseEntity<FriendResponse> expectedResponse = new ResponseEntity<>(new FriendResponse(), HttpStatus.OK);
        when(friendsService.getFriendsOfUser(1L)).thenAnswer(invocation -> ResponseEntity.ok(new FriendResponse()));
        ResponseEntity<FriendResponse> actualResponse = friendsController.getFriendsOfUser(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getFriendsOfUser(1L);
    }

    @Test
    public void testCheckFriendShip() {
        ResponseEntity<CheckFriendShipResponse> expectedResponse = new ResponseEntity<>(new CheckFriendShipResponse(), HttpStatus.OK);
        when(friendsService.checkFriendship(1L)).thenAnswer(invocation -> ResponseEntity.ok(new CheckFriendShipResponse()));
        ResponseEntity<CheckFriendShipResponse> actualResponse = friendsController.checkFriendShip(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).checkFriendship(1L);
    }
    @Test
    public void testAddFriendRequest() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);
        when(friendsService.addFriendRequest(1L)).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = friendsController.addFriendRequest(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).addFriendRequest(1L);
    }
    @Test
    public void testGetRequestUserNotAccepte() {
        ResponseEntity<FriendResponse> expectedResponse = new ResponseEntity<>(new FriendResponse(), HttpStatus.OK);
        when(friendsService.getUsersNotAcceptedRequests(1L)).thenAnswer(invocation -> ResponseEntity.ok(new FriendResponse()));
        ResponseEntity<FriendResponse> actualResponse = friendsController.getRequestUserNotAccepte(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getUsersNotAcceptedRequests(1L);
    }
    @Test
    public void testGetRequestNotAccepteToUser() {
        ResponseEntity<FriendResponse> expectedResponse = new ResponseEntity<>(new FriendResponse(), HttpStatus.OK);
        when(friendsService.getNotAcceptedRequestsToUser(1L)).thenAnswer(invocation -> ResponseEntity.ok(new FriendResponse()));
        ResponseEntity<FriendResponse> actualResponse = friendsController.getRequestNotAccepteToUser(1L);
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).getNotAcceptedRequestsToUser(1L);
    }
    @Test
    public void testDeleteFriendship() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);
        when(friendsService.deleteFriendship(new FriendShipRequestDTO())).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = friendsController.deleteFriendship(new FriendShipRequestDTO());
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).deleteFriendship(new FriendShipRequestDTO());
    }
    @Test
    public void testAccepteFriendship() {
        ResponseEntity<ResponseOk> expectedResponse = new ResponseEntity<>(new ResponseOk(), HttpStatus.OK);
        when(friendsService.accepteFriendShip(new FriendShipRequestDTO())).thenAnswer(invocation -> ResponseEntity.ok(new ResponseOk()));
        ResponseEntity<?> actualResponse = friendsController.accepteFriendship(new FriendShipRequestDTO());
        assertEquals(expectedResponse, actualResponse);
        verify(friendsService, times(1)).accepteFriendShip(new FriendShipRequestDTO());
    }

}
