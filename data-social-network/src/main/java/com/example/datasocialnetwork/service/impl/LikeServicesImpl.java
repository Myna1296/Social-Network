package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.entity.LikeStatus;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.*;
import com.example.datasocialnetwork.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeServicesImpl implements LikeService {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public ResponseEntity<?> addLike(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.STATUS_NOT_FOUND);
        }
        LikeStatus like = likeRepository.findLikesByStatusIdAndUserId(id,Long.parseLong(userDetails.getUserID()));
        if (like != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.LIKE_ERR);
        }
        LikeStatus likeStatus = new LikeStatus();
        likeStatus.setStatus(status);
        likeStatus.setUser(user);
        likeStatus.setCreatedDate(LocalDateTime.now());

        likeRepository.save(likeStatus);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.LIKE_SUCCESS);
    }

    @Override
    public ResponseEntity<?> deleteLike(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.STATUS_NOT_FOUND);
        }
        LikeStatus like = likeRepository.findLikesByStatusIdAndUserId(id,user.getId());
        if (like == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.UNLIKE_ERR);
        }
        likeRepository.delete(like);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.UNLIKE_SUCCESS);
    }
}

