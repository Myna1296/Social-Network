package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.request.NewCommentRequest;
import org.springframework.http.ResponseEntity;

public interface CommentService {
    ResponseEntity<?> searchCommentByStatusId(CommentRequest commentRequest);
    ResponseEntity<?> addNewCommen(NewCommentRequest newCommentRequest);
    ResponseEntity<?> deleteCommnet(Long id);
}
