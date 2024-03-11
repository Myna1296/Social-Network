package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/friends")
public class FriendsController {

   @Autowired
   private FriendsServiceImpl friendsService;

   @PostMapping("/get-all")
   public ResponseEntity<FriendResponse> getFriendsOfUser(@RequestBody FriendRequestDTO friendRequestDTO ){
      return friendsService.getFriendsOfUser(friendRequestDTO);
   }
}
