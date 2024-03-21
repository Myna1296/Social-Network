package com.example.datasocialnetwork.service;

import org.springframework.http.ResponseEntity;

public interface LikeService {
    ResponseEntity<?> addLike(Long id);
    ResponseEntity<?> deleteLike(Long id);
}
