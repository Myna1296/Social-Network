package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.CommentDTO;
import com.example.datasocialnetwork.dto.request.CommentRequest;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> searchCommentByStatusId(CommentRequest commentRequest);
    ResponseEntity<?> addNewCommen(CommentDTO commentDTO);
    ResponseEntity<?> deleteCommnet(Long id);
}
