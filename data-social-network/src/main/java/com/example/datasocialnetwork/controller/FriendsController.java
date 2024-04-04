package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.service.impl.FriendsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/friends")
public class FriendsController {

   @Autowired
   private FriendsServiceImpl friendsService;

   @GetMapping("/accepte-friend-request")
   public ResponseEntity<?> getFriendsOfUser(@RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex,
                                                          @RequestParam(name = "pageSize", defaultValue = "5") int pageSize){
      return friendsService.getAccepteFriendRequest(pageIndex, pageSize);
   }

   @PostMapping("/check-friend-ship/{userId}")
   public ResponseEntity<?> checkFriendShip(@PathVariable("userId")  Long id ){
      return friendsService.checkFriendship(id);
   }

   @PostMapping("/sent-friend-request/{id}")
   public ResponseEntity<?> addFriendRequest(@PathVariable("id") Long idTarget){
      return friendsService.addFriendRequest(idTarget);
   }

   @GetMapping("/user-sent-friend-request")
   public ResponseEntity<?> getUserSentFriendRequest(@RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex,
                                                                  @RequestParam(name = "pageSize", defaultValue = "5") int pageSize){
      return friendsService.getUserSentFriendRequest(pageIndex, pageSize);
   }

   @GetMapping("/waiting-user-to-accept")
   public ResponseEntity<?> getWaitingUserToAccept(@RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex,
                                                       @RequestParam(name = "pageSize", defaultValue = "5") int pageSize){
      return friendsService.getWaitingUserToAccept(pageIndex, pageSize);
   }

   @DeleteMapping("/delete")
   public ResponseEntity<?> deleteFriendship(@RequestBody FriendShipRequestDTO friendShipRequestDTO){
      return friendsService.deleteFriendship(friendShipRequestDTO);
   }

   @PutMapping("/accepte/{id}")
   public ResponseEntity<?> accepteFriendship(@PathVariable("id") Long idTarget){
      return friendsService.accepteFriendShip(idTarget);
   }


}