package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FriendsService {
    ResponseEntity<FriendResponse> getFriendsOfUser(Long pageId);
    ResponseEntity<CheckFriendShipResponse> checkFriendship(Long id);
    ResponseEntity<?> addFriendRequest(Long idTarget);
    ResponseEntity<FriendResponse> getUsersNotAcceptedRequests(Long pageId);
    ResponseEntity<FriendResponse> getNotAcceptedRequestsToUser(Long pageId);
    ResponseEntity<?> deleteFriendship(FriendShipRequestDTO friendShipRequestDTO);
    ResponseEntity<?> accepteFriendShip(FriendShipRequestDTO friendShipRequestDTO);
}
