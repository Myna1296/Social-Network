package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.FriendShipRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.FriendShipRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.FriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.datasocialnetwork.common.Gender.getGenderById;

@Service
public class FriendsServiceImpl implements FriendsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendShipRepository friendShipRepository;

    @Override
    public ResponseEntity<?> getAccepteFriendRequest(int pageIndex, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Page<FriendShip> pageFriends = friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageIndex - 1, pageSize)
        );

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserSender().getId().equals(user.getId())) {
                        return Stream.of(convertUserToUserInfo(friendShip.getUserReceiver()));
                    } else {
                        return Stream.of(convertUserToUserInfo(friendShip.getUserSender()));
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countAcceptedFriendshipsByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> checkFriendship(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        if (Long.parseLong(userDetails.getUserID()) == id){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.CHECK_FRIENDSHIP_ERR1);
        }
        User userFriend = userRepository.findOneById(id);
        if (userFriend == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Boolean checkFriendShip = friendShipRepository.checkFriendshipExists(user, userFriend);
        String jsonResponse = "{\"isFriendShip\": " + checkFriendShip + "}";
        return ResponseEntity.status(HttpStatus.OK).body(jsonResponse);
    }

    @Override
    public ResponseEntity<?> addFriendRequest(Long idTarget) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));

        if (user.getId() == idTarget){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FRIEND_ADD_ERR1);
        }
        User userTarget = userRepository.findOneById(idTarget);
        if (userTarget == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }
        if(friendShipRepository.checkFriendshipExists(user, userTarget)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FRIEND_ADD_ERR2);
        } else {
            if (friendShipRepository.checkFriendshipRequest(user,userTarget)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FRIEND_ADD_ERR3);
            } else {
                FriendShip friendship = new FriendShip();
                friendship.setUserSender(user);
                friendship.setUserReceiver(userTarget);
                friendship.setAccepted(false);
                friendship.setCreatedDate(LocalDateTime.now());
                friendShipRepository.save(friendship);
                return ResponseEntity.status(HttpStatus.OK).body(Constants.FRIEND_ADD_SUCCESS);
            }
        }
    }

    @Override
    public ResponseEntity<?> getUserSentFriendRequest(int pageIndex, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Page<FriendShip> pageFriends = friendShipRepository.findUsersNotAcceptedRequestsByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageIndex - 1, pageSize));

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserSender().getId().equals(user.getId())) {
                        return Stream.of(convertUserToUserInfo(friendShip.getUserReceiver()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countUsersNotAcceptedRequestsByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> getWaitingUserToAccept(int pageIndex, int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Page<FriendShip> pageFriends = friendShipRepository.findNotAcceptedRequestsToUserByUserIdWithLimitOffset(
                user.getId(),
                PageRequest.of(pageIndex - 1, pageSize)
        );

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserReceiver().getId().equals(user.getId())) {
                        return Stream.of(convertUserToUserInfo(friendShip.getUserSender()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countNotAcceptedRequestsToUserByUserId(user.getId());
        FriendResponse response = new FriendResponse();
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> deleteFriendship(FriendShipRequestDTO friendShipRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));

        if (user.getId() == friendShipRequestDTO.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể xóa kết bạn với chính bản thân");
        }
        User userFriend = userRepository.findOneById(friendShipRequestDTO.getId());
        if (userFriend == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }
        List<FriendShip> friendShipList = friendShipRepository.checkFriendshipExists(user, userFriend, friendShipRequestDTO.isAccepte());
        if(friendShipList.isEmpty()) {
            String mess = friendShipRequestDTO.isAccepte()? "Không tồn tại kết bạn với ngừi dùng này" : " Không tồn tại lời mời kết bạn với người dùng này";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mess);
        } else {
                FriendShip friendShip = friendShipList.get(0);
                friendShipRepository.delete(friendShip);
                String mess = friendShipRequestDTO.isAccepte()? "Đã xóa bạn bè thành công" : " Đã xóa lời mời kết bạn thành công";
                return ResponseEntity.status(HttpStatus.OK).body(mess);
        }
    }

    @Override
    public ResponseEntity<?> accepteFriendShip(Long idTarget) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        if (user.getId() == idTarget){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không thể  kết bạn với chính bản thân");
        }
        User userFriend = userRepository.findOneById(idTarget);
        if (userFriend == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }

        List<FriendShip> friendShipList = friendShipRepository.checkFriendshipExists(user, userFriend, false);
        if(friendShipList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tồn tại lời mời kết bạn với người dùng này");
        } else {
            FriendShip friendShip = friendShipList.get(0);
            friendShip.setAccepted(true);
            friendShipRepository.save(friendShip);
            return ResponseEntity.status(HttpStatus.OK).body("Đã kết bạn thành công");
        }
    }

    private UserInfo convertUserToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(String.valueOf(user.getId()));
        userInfo.setUserName(user.getUserName());
        userInfo.setBirthday(user.getBirthday() == null ? null : user.getBirthday().toString());
        userInfo.setAddress(user.getAddress());
        userInfo.setJob(user.getJob());
        userInfo.setSex(getGenderById(user.getSex()).name());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvata(user.getImage());

        return userInfo;
    }
}
