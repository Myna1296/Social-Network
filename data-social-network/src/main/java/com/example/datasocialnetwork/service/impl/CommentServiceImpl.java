package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.CommentInfo;
import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.request.NewCommentRequest;
import com.example.datasocialnetwork.dto.response.CommentListResponse;
import com.example.datasocialnetwork.entity.Comment;
import com.example.datasocialnetwork.entity.Status;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.CommentRepository;
import com.example.datasocialnetwork.repository.StatusRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.CommentService;
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
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> searchCommentByStatusId(CommentRequest commentRequest) {
        Status status = statusRepository.findStatusById(commentRequest.getStatusId());
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.STATUS_NOT_FOUND);
        }
        Page<Comment> pageComment = commentRepository.findCommentsByStatusId(
                commentRequest.getStatusId(),
                PageRequest.of(commentRequest.getPageIndex() - 1, commentRequest.getPageSize())
        );
        CommentListResponse response =  convertPageToResponse(pageComment);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<?> addNewCommen(NewCommentRequest newCommentRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));

        Status status = statusRepository.findStatusById(newCommentRequest.getStatusId());
        if (status == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.STATUS_NOT_FOUND);
        }
        Comment comment = new Comment();
        comment.setCommentText(newCommentRequest.getContent());
        comment.setUser(user);
        comment.setStatus(status);
        comment.setCreatedDate(LocalDateTime.now());
        commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.OK).body(Constants.COMMENT_ADD_SUCCESS);
    }

    @Override
    public ResponseEntity<?> deleteCommnet(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));

        Comment comment = commentRepository.findOneById(id);
        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.COMMENT_NOT_FOUND);
        }
        if (comment.getUser().getId() != user.getId()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.COMMENT_FORBIDDEN);
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).body(Constants.COMMENT_DELETE_SUCCESS);
    }

    private static CommentInfo convertCommontToDTO(Comment comment) {
        CommentInfo commentInfo = new CommentInfo();
        commentInfo.setId(comment.getId());
        commentInfo.setContent(comment.getCommentText());
        commentInfo.setCreateDate(comment.getCreatedDate().toString());
        commentInfo.setUserId(comment.getUser().getId());
        commentInfo.setStatusId(comment.getStatus().getId());
        commentInfo.setUserName(comment.getUser().getUserName());
        commentInfo.setUserAvata(comment.getUser().getImage()); // Assuming this field exists in User entity
        return commentInfo;
    }

    private static CommentListResponse convertPageToResponse(Page<Comment> commentPage) {
        CommentListResponse response = new CommentListResponse();
        response.setTotalPage(commentPage.getTotalPages());
        List<CommentInfo> commentDTOList = new ArrayList<>();
        for (Comment comment : commentPage.getContent()) {
            CommentInfo commentInfo = convertCommontToDTO(comment);
            commentDTOList.add(commentInfo);
        }
        response.setListComment(commentDTOList);
        return response;
    }
}
