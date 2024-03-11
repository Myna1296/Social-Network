package com.example.datasocialnetwork.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    //private final CommentService commentService;

//    @PostMapping("/{postId}")
//    public CommentResponse addComment(@RequestBody @Valid CommentSaveRequest commentSaveRequest, @PathVariable("postId") Long postId){
//        return commentService.save(commentSaveRequest,postId);
//    }
//
//    @PostMapping("/update")
//    public CommentResponse updateComment(@RequestBody @Valid CommentUpdateRequest commentUpdateRequest){
//        return commentService.update(commentUpdateRequest);
//    }
//
//    @PostMapping("/delete/{commentId}")
//    public void deleteComment(@PathVariable("commentId") Long commentId){
//        commentService.delete(commentId);
//    }
//
//    @PostMapping("/all/{postId}")
//    public Page<CommentResponse> getAll(@PathVariable("postId") Long postId, @RequestBody PageInfo pageInfo){
//        PageRequest pageRequest = PageRequest.of(pageInfo.getIndex(), pageInfo.getSize());
//        return commentService.findAllByPost(postId,pageRequest);
//    }
}
