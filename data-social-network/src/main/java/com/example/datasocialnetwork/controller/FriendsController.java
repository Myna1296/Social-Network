package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/friends")
public class FriendsController {

   @Autowired
   private FriendsServiceImpl friendsService;

   @GetMapping("/get-friend/{pageId}")
   public ResponseEntity<FriendResponse> getFriendsOfUser(@PathVariable("pageId")  Long id  ){
      return friendsService.getFriendsOfUser(id);
   }

   @PostMapping("/check-friend-ship/{userId}")
   public ResponseEntity<?> checkFriendShip(@PathVariable("userId")  Long id ){
      return friendsService.checkFriendship(id);
   }

   @PostMapping("/add-to-friends/{id}")
   public ResponseEntity<?> addFriendRequest(@PathVariable("id") Long idTarget){
      return friendsService.addFriendRequest(idTarget);
   }

   @GetMapping("/get-request-user-notaccepte/{pageId}")
   public ResponseEntity<FriendResponse> getRequestUserNotAccepte(@PathVariable("pageId")  Long id ){
      return friendsService.getUsersNotAcceptedRequests(id);
   }

   @GetMapping("/get-request-notaccepte-touser/{pageId}")
   public ResponseEntity<FriendResponse> getRequestNotAccepteToUser(@PathVariable("pageId")  Long id ){
      return friendsService.getNotAcceptedRequestsToUser(id);
   }

   @DeleteMapping("/delete")
   public ResponseEntity<?> deleteFriendship(@RequestBody FriendShipRequestDTO friendShipRequestDTO){
      return friendsService.deleteFriendship(friendShipRequestDTO);
   }

   @PostMapping("/accepte")
   public ResponseEntity<?> accepteFriendship(@RequestBody FriendShipRequestDTO friendShipRequestDTO){
      return friendsService.accepteFriendShip(friendShipRequestDTO);
   }


}