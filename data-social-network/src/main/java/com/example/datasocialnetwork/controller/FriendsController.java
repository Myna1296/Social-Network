package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/friends")
public class FriendsController {

   @Autowired
   private FriendsServiceImpl friendsService;

   @PostMapping("/get-all")
   public ResponseEntity<FriendResponse> getFriendsOfUser(@RequestBody FriendRequestDTO friendRequestDTO ){
      return friendsService.getFriendsOfUser(friendRequestDTO);
   }

   @PostMapping("/check-friend-ship/{id}")
   public ResponseEntity<CheckFriendShipResponse> checkFriendShip(@PathVariable("id")  Long id ){
      return friendsService.checkFriendship(id);
   }

   @PostMapping("/add-to-friends/{id}")
   public ResponseEntity<?> addFriendRequest(@PathVariable("id") Long idTarget){
      return friendsService.addFriendRequest(idTarget);
   }
}