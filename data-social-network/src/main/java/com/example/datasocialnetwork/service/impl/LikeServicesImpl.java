package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.response.ResponseOk;
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
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_013);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        LikeStatus like = likeRepository.findLikesByStatusIdAndUserId(id,user.getId());
        if (like != null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("I've already liked this post, but I can't like it again.");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        LikeStatus likeStatus = new LikeStatus();
        likeStatus.setStatus(status);
        likeStatus.setUser(user);
        likeStatus.setCreatedDate(LocalDateTime.now());

        likeRepository.save(likeStatus);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteLike(Long id) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_013);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        LikeStatus like = likeRepository.findLikesByStatusIdAndUserId(id,user.getId());
        if (like == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Haven't liked the post yet, can't unlike it");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        likeRepository.delete(like);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

