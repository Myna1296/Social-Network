package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.CommentDTO;
import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.response.CommentListResponse;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.StatusAllResponse;
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
            CommentListResponse response = new CommentListResponse();
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Page<Comment> pageComment = commentRepository.findCommentsByStatusId(
                commentRequest.getStatusId(),
                PageRequest.of(commentRequest.getPage() - 1, Constants.LIMIT)
        );
        CommentListResponse response =  convertPageToResponse(pageComment);
        response.setPage(commentRequest.getPage());
        response.setCode(Constants.CODE_OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addNewCommen(CommentDTO commentDTO) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (user.getId() != commentDTO.getUserId()){
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Authentication information does not match");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Status status = statusRepository.findStatusById(commentDTO.getStatusId());
        if (status == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Status does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Comment comment = new Comment();
        comment.setCommentText(commentDTO.getContent());
        comment.setUser(user);
        comment.setStatus(status);
        comment.setCreatedDate(LocalDateTime.now());
        commentRepository.save(comment);

        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteCommnet(Long id) {
        ResponseOk response = new ResponseOk();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Comment comment = commentRepository.findOneById(id);
        if (comment == null) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("Comment does not exist");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (comment.getUser().getId() != user.getId()) {
            response.setCode(Constants.CODE_ERROR);
            response.setMessage("This comment is not allowed to be deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        commentRepository.delete(comment);

        response.setCode(Constants.CODE_OK);
        response.setMessage("");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static CommentDTO convertCommontToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getCommentText());
        commentDTO.setCreateDate(comment.getCreatedDate().toString());
        commentDTO.setUserId(comment.getUser().getId());
        commentDTO.setStatusId(comment.getStatus().getId());
        commentDTO.setUserName(comment.getUser().getUserName());
        commentDTO.setUserAvata(comment.getUser().getImage()); // Assuming this field exists in User entity
        return commentDTO;
    }

    private static CommentListResponse convertPageToResponse(Page<Comment> commentPage) {
        CommentListResponse response = new CommentListResponse();
        response.setTotalPage(commentPage.getTotalPages());
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentPage.getContent()) {
            CommentDTO commentDTO = convertCommontToDTO(comment);
            commentDTOList.add(commentDTO);
        }
        response.setListComment(commentDTOList);
        return response;
    }
}
