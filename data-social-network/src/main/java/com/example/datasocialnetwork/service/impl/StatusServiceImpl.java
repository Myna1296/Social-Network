package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.FriendResponse;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.StatusAllResponse;
import com.example.datasocialnetwork.dto.response.StatusInfoResponse;
import com.example.datasocialnetwork.entity.FriendShip;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.StatusRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public ResponseEntity<?> getStatusByUserId(StatusRequest statusRequest) {
        User user = userRepository.findOneById(statusRequest.getUserId());
        if (user == null) {
            StatusAllResponse response = new StatusAllResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<Status> pageStatus = statusRepository.findStatusByUserIdWithLimitOffset(
                statusRequest.getUserId(),
                PageRequest.of(statusRequest.getPage() - 1, Constants.LIMIT)
        );

        StatusAllResponse response = convertPageToResponse(pageStatus);
        response.setUserName(user.getUserName());
        response.setPage(statusRequest.getPage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getStatusFriendUser(StatusRequest statusRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            StatusAllResponse response = new StatusAllResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else  if (user.getId() != statusRequest.getUserId()){
            StatusAllResponse response = new StatusAllResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Authentication information does not match");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<Status> pageStatus = statusRepository.findStatusOfAllFriends(
                statusRequest.getUserId(),
                PageRequest.of(statusRequest.getPage() - 1, Constants.LIMIT)
        );

        StatusAllResponse response = convertPageToResponse(pageStatus);
        response.setUserName(user.getUserName());
        response.setPage(statusRequest.getPage());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteStatus(Long id) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if( status.getUser().getId() != user.getId()){
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Cannot delete other people's status");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        statusRepository.delete(status);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addNewStatus(StatusDTO statusDTO) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userAddStatus = userRepository.findOneById(statusDTO.getUserId());
        if (userAddStatus == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() != statusDTO.getUserId()) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Authentication information does not match");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = new Status();
        status.setTitle(statusDTO.getTitle());
        status.setStatusText(statusDTO.getContent());
        status.setCreatedDate(LocalDateTime.now());
        status.setStatusImage(statusDTO.getStatusImage());
        status.setUser(userAddStatus);
        statusRepository.save(status);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateStatus(StatusDTO statusDTO) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User userAddStatus = userRepository.findOneById(statusDTO.getUserId());
        if (userAddStatus == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (user.getId() != statusDTO.getUserId()) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Authentication information does not match");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(statusDTO.getId());
        if (status == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        status.setTitle(statusDTO.getTitle());
        status.setStatusText(statusDTO.getContent());
        status.setCreatedDate(LocalDateTime.now());
        status.setStatusImage(statusDTO.getStatusImage());
        statusRepository.save(status);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchStatus(Long id) {
        StatusInfoResponse response = new StatusInfoResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(id);
        StatusDTO statusDTO = convertStatusToDTO(status);
        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        response.setStatus(statusDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static StatusDTO convertStatusToDTO(Status status) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(status.getId());
        statusDTO.setTitle(status.getTitle());
        statusDTO.setContent(status.getStatusText());
        statusDTO.setStatusImage(status.getStatusImage());
        statusDTO.setCountLike(status.getLikeCount());
        statusDTO.setCountComment(status.getCommentCount());
        statusDTO.setCreateDate(status.getCreatedDate().toString());
        statusDTO.setUserId(status.getUser().getId());
        statusDTO.setUserName(status.getUser().getUserName());
        statusDTO.setUserAvata(status.getUser().getImage()); // Assuming this field exists in User entity
        return statusDTO;
    }

    private static StatusAllResponse convertPageToResponse(Page<Status> statusPage) {
        StatusAllResponse response = new StatusAllResponse();
        response.setTotalPage(statusPage.getTotalPages());
        List<StatusDTO> statusDTOList = new ArrayList<>();
        for (Status status : statusPage.getContent()) {
            StatusDTO statusDTO = convertStatusToDTO(status);
            statusDTOList.add(statusDTO);
        }
        response.setListStatus(statusDTOList);
        return response;
    }
}
