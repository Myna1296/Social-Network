package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FriendsService {
    ResponseEntity<?> getAccepteFriendRequest(int pageIndex, int pageSize);
    ResponseEntity<?> checkFriendship(Long id);
    ResponseEntity<?> addFriendRequest(Long idTarget);
    ResponseEntity<?> getUserSentFriendRequest(int pageIndex, int pageSize);
    ResponseEntity<?> getWaitingUserToAccept(int pageIndex, int pageSize);
    ResponseEntity<?> deleteFriendship(FriendShipRequestDTO friendShipRequestDTO);
    ResponseEntity<?> accepteFriendShip(Long idTarget);
}
