package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FriendsService {
    ResponseEntity<FriendResponse> getFriendsOfUser(FriendRequestDTO friendRequestDTO);
    ResponseEntity<CheckFriendShipResponse> checkFriendship(Long id);
    ResponseEntity<?> addFriendRequest(Long idTarget);
    ResponseEntity<FriendResponse> getUsersNotAcceptedRequests(FriendRequestDTO friendRequestDTO);
    ResponseEntity<FriendResponse> getNotAcceptedRequestsToUser(FriendRequestDTO friendRequestDTO);
    ResponseEntity<?> deleteFriendship(Long id);
    ResponseEntity<?> Friendshipaccepte(Long id);
}
