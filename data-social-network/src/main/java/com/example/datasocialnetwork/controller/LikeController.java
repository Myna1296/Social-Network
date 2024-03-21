package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.service.impl.ExportServicesImpl;
import com.example.datasocialnetwork.service.impl.LikeServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    LikeServicesImpl likeServices;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addLike(@PathVariable("id")Long id) {
        return likeServices.addLike(id);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteLike(@PathVariable("id") Long id) {
        return likeServices.deleteLike(id);
    }
}
