package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface FriendsService {
    ResponseEntity<FriendResponse> getFriendsOfUser(FriendRequestDTO friendRequestDTO);
}
