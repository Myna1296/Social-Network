package com.example.datasocialnetwork.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.StatusAllResponse;
import com.example.datasocialnetwork.dto.response.StatusInfoResponse;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.CommentRepository;
import com.example.datasocialnetwork.repository.LikeRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatusServiceImpl implements StatusService {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public ResponseEntity<?> getNewsFeed (int pageIndex,int pageSize) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        Page<Status> pageStatus = statusRepository.findStatusOfAllFriends(
                Long.parseLong(userDetails.getUserID()),
                PageRequest.of(pageIndex - 1, pageSize)
        );

        StatusAllResponse response  = convertPageToResponse(pageStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> getStatusByUserId(StatusRequest statusRequest) {
        if (statusRequest == null || statusRequest.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("VUi lòng nhập đủ thông tin request yêu cầu");
        }
        User user = userRepository.findOneById(statusRequest.getUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }
        Page<Status> pageStatus = statusRepository.findStatusByUserIdWithLimitOffset(
                statusRequest.getUserId(),
                PageRequest.of(statusRequest.getPageIndex() - 1, statusRequest.getPageSize())
        );

        StatusAllResponse response  = convertPageToResponse(pageStatus);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteStatus(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        Status status = statusRepository.findStatusById(id);
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.STATUS_NOT_FOUND);
        }
        if (status.getUser().getId() != Long.parseLong(userDetails.getUserID())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.STATUS_DELETE_FORBIDDEN);
        }
        likeRepository.deleteLikeStatusByStatusId(id);
        commentRepository.deleteCommentByStatusId(id);
        statusRepository.delete(status);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.STATUS_DELETE_SUCCESS);
    }

    @Override
    public ResponseEntity<?> addNewStatus(NewStatusRequest newStatusRequest) {
        List<String> listErr = validateStatus(newStatusRequest.getTitle(), newStatusRequest.getContent());
        if (!listErr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listErr);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.USER_NOT_FOUND);
        }
        try {
            Status status = new Status();
            if( newStatusRequest.getImage() != null) {
                MultipartFile fileImage =  newStatusRequest.getImage();
                if (!isImageFile(fileImage.getContentType())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FILE_IS_NOT_FORMAT);
                }
                Map data = this.cloudinary.uploader().upload(fileImage.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String url = (String) data.get("secure_url");
                status.setStatusImage(url);
            }
            status.setTitle(newStatusRequest.getTitle());
            status.setStatusText(newStatusRequest.getContent());
            status.setCreatedDate(LocalDateTime.now());
            status.setUser(user);
            statusRepository.save(status);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.ADD_NEW_STATUS_SUCCESS);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> updateStatus(UpdateStatusRequest updateStatusRequest) {
        List<String> listErr = validateStatus(updateStatusRequest.getTitle(), updateStatusRequest.getContent());
        if (!listErr.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listErr);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        Status status = statusRepository.findStatusById(updateStatusRequest.getId());
        if (status == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.STATUS_NOT_FOUND);
        }
        if (status.getUser().getId() != Long.parseLong(userDetails.getUserID())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.STATUS_UPDATE_FORBIDDEN);
        }
        try {
            if (updateStatusRequest.getImage() != null){
                if (!isImageFile(updateStatusRequest.getImage().getContentType())) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FILE_IS_NOT_FORMAT);
                }
            Map data = this.cloudinary.uploader().upload(updateStatusRequest.getImage().getBytes(), ObjectUtils.asMap("resource_type", "auto"));
            String url = (String) data.get("secure_url");
            status.setStatusImage(url);
            }
            status.setTitle(updateStatusRequest.getTitle());
            status.setStatusText(updateStatusRequest.getContent());
            status.setCreatedDate(LocalDateTime.now());
            statusRepository.save(status);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.STATUS_UPDATE_SUCCESS);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> searchStatus(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        Status status = statusRepository.findStatusById(id);
        if(status == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.STATUS_NOT_FOUND);
        }
        long count = likeRepository.countByStatusIdAndUserId(id, Long.parseLong(userDetails.getUserID()));
        StatusInfoResponse statusInfoResponse = new StatusInfoResponse();
        statusInfoResponse.setStatus(convertStatusToDTO(status));
        //response.setStatus(statusDTO);
        statusInfoResponse.setLike( count!= 0);
        return ResponseEntity.status(HttpStatus.OK).body(statusInfoResponse);
    }

    private static StatusInfo convertStatusToDTO(Status status) {
        StatusInfo statusDTO = new StatusInfo();
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
        List<StatusInfo> statusDTOList = new ArrayList<>();
        for (Status status : statusPage.getContent()) {
            StatusInfo statusDTO = convertStatusToDTO(status);
            statusDTOList.add(statusDTO);
        }
        response.setListStatus(statusDTOList);
        return response;
    }

    private static boolean isImageFile(String contentType) throws IOException {;
        return contentType != null &&
                (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
    private List<String> validateStatus(String title, String content){
        List<String> listErr = new ArrayList<>();
        if(title == null){
            listErr.add("Title cannot be null");
        }
        if(title.isEmpty()){
            listErr.add("Title cannot be empty");
        }
        if(title.trim().isEmpty()){
            listErr.add("Title cannot be blank");
        }
        if(title.length() > 50){
            listErr.add("Title must be between 1 and 50 words in length");
        }
        if(content == null){
            listErr.add("Content cannot be null");
        }
        if(content.isEmpty()){
            listErr.add("Content cannot be empty");
        }
        if(content.trim().isEmpty()){
            listErr.add("Content cannot be blank");
        }
        if(content.length() > 5000){
            listErr.add("Content must be between 1 and 5000 words in length");
        }
        return listErr;
    }

}
