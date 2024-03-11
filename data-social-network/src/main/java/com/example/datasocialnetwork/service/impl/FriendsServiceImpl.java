package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.FriendRequestDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.FriendShipRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.FriendsService;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private int LIMIT = 3;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<FriendResponse> getFriendsOfUser(FriendRequestDTO friendRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(friendRequestDTO.getId());
        if (user == null) {
            FriendResponse response = new FriendResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_004);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else  if (!userDetails.getUsername().equals(user.getUserName())){
            throw(new UserNotFoundException("Authentication information does not match"));
        }
        Page<FriendShip> pageFriends = friendShipRepository.findAcceptedFriendshipsByUserIdWithLimitOffset(
                friendRequestDTO.getId(),
                PageRequest.of(friendRequestDTO.getPage() - 1, LIMIT)
        );

        List<UserInfo> friendsOfUser = pageFriends.stream()
                .flatMap(friendShip -> {
                    if (friendShip.getUserSender().getId().equals(friendRequestDTO.getId())) {
                        return Stream.of(convertToUserInfo(friendShip.getUserReceiver()));
                    } else if (friendShip.getUserReceiver().getId().equals(friendRequestDTO.getId())) {
                        return Stream.of(convertToUserInfo(friendShip.getUserSender()));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
        Long total = friendShipRepository.countAcceptedFriendshipsByUserId(friendRequestDTO.getId());
        FriendResponse response = new FriendResponse();
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        response.setFriendData(friendsOfUser);
        response.setTotal(total);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static UserInfo convertToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(String.valueOf(user.getId()));
        userInfo.setUserName(user.getUserName());
        userInfo.setAvata(user.getImage());
        return userInfo;
    }
}
