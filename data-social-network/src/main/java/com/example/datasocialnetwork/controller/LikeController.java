package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.service.impl.LikeServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired
    LikeServicesImpl likeServices;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addLike(@PathVariable("id")Long id) {
        return likeServices.addLike(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLike(@PathVariable("id") Long id) {
        return likeServices.deleteLike(id);
    }
}
