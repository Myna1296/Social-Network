package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.common.Gender;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.CheckFriendShipResponse;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.exceptions.BadRequestException;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.FriendShipRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.FriendsService;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
import javafx.scene.input.KeyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FriendsServiceImpl implements FriendsService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendShipRepository friendShipRepository;

    @Override
    public ResponseEntity<FriendResponse> getFriendsOfUser(Long pageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            FriendResponse response = new FriendResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<FriendShip> pageFriends = friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageId.intValue() - 1, Constants.LIMIT)
        );

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserSender().getId().equals(user.getId())) {
                        return Stream.of(convertToUserInfo(friendShip.getUserReceiver()));
                    } else {
                        return Stream.of(convertToUserInfo(friendShip.getUserSender()));
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countAcceptedFriendshipsByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CheckFriendShipResponse> checkFriendship(Long id) {
        CheckFriendShipResponse response = new CheckFriendShipResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() == id){
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Do not check friends ships with yourself");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userFriend = userRepository.findOneById(id);
        if (userFriend == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Friend does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Boolean checkFriendShip = friendShipRepository.checkFriendshipExists(user, userFriend);
        response.setCode(Constants.CODE_OK);
        response.setCheckFriendShip(checkFriendShip);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addFriendRequest(Long idTarget) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"User does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() == idTarget){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"Can't send friend requests to myself");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userTarget = userRepository.findOneById(idTarget);
        if (userTarget == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"Friend does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(friendShipRepository.checkFriendshipExists(user, userTarget)) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "Already friends");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            if (friendShipRepository.checkFriendshipRequest(user,userTarget)) {
                ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "Friend request already exists");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                FriendShip friendship = new FriendShip();
                friendship.setUserSender(user);
                friendship.setUserReceiver(userTarget);
                friendship.setAccepted(false);
                friendship.setCreatedDate(LocalDateTime.now());
                friendShipRepository.save(friendship);
                ResponseOk response = new ResponseOk(Constants.CODE_OK, "Send friend request successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<FriendResponse> getUsersNotAcceptedRequests(Long pageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            FriendResponse response = new FriendResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<FriendShip> pageFriends = friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageId.intValue() - 1, Constants.LIMIT));

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserSender().getId().equals(user.getId())) {
                        return Stream.of(convertToUserInfo(friendShip.getUserReceiver()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countUsersNotAcceptedRequestsByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<FriendResponse> getNotAcceptedRequestsToUser(Long pageId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            FriendResponse response = new FriendResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<FriendShip> pageFriends = friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageId.intValue() - 1, Constants.LIMIT)
        );

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserReceiver().getId().equals(user.getId())) {
                        return Stream.of(convertToUserInfo(friendShip.getUserSender()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countNotAcceptedRequestsToUserByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFriendship(FriendShipRequestDTO friendShipRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"User does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() == friendShipRequestDTO.getId()){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"This friend request does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userFriend = userRepository.findOneById(friendShipRequestDTO.getId());
        if (userFriend == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"Friend does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        List<FriendShip> friendShipList = friendShipRepository.checkFriendshipExists(user, userFriend, friendShipRequestDTO.isAccepte());
        if(friendShipList.isEmpty()) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "Friend request does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
                FriendShip friendShip = friendShipList.get(0);
                friendShipRepository.delete(friendShip);
                ResponseOk response = new ResponseOk(Constants.CODE_OK, "Delete friend request successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> accepteFriendShip(FriendShipRequestDTO friendShipRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"User does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() == friendShipRequestDTO.getId()){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"This friend request does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userFriend = userRepository.findOneById(friendShipRequestDTO.getId());
        if (userFriend == null) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,"Friend does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<FriendShip> friendShipList = friendShipRepository.checkFriendshipExists(user, userFriend, friendShipRequestDTO.isAccepte());
        if(friendShipList.isEmpty()) {
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "Friend request does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            FriendShip friendShip = friendShipList.get(0);
            friendShip.setAccepted(true);
            friendShipRepository.save(friendShip);
            ResponseOk response = new ResponseOk(Constants.CODE_OK, "Accepte friend request successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    private static UserInfo convertToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(String.valueOf(user.getId()));
        userInfo.setUserName(user.getUserName());
        userInfo.setAvata(user.getImage());
        return userInfo;
    }
}
