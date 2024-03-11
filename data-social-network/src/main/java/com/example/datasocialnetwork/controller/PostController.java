package com.example.datasocialnetwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
//    private final PostService postService;
//    private final Profile profile;
//    @PostMapping
//    public PostResponse savePost(@Valid @RequestBody PostSaveRequest postSaveRequest){
//        return postService.save(postSaveRequest);
//    }
//    @PostMapping("/update/{id}") // update post
//    public PostResponse updatePost(@PathVariable("id") Long idPost,@RequestBody @Valid PostSaveRequest postSaveRequest){
//        return postService.update(idPost,postSaveRequest);
//    }
//
//    @PostMapping("/delete/{id}") // delete post
//    public void deletePost(@PathVariable("id") Long idPost){
//        postService.delete(idPost);
//    }
//
//    @GetMapping("/{postId}")
//    public PostResponse findOneById(@PathVariable("postId") Long postId){
//        return postService.findOneById(postId);
//    }
//
//    @PostMapping("/all/{userId}")
//    public Page<PostResponse> getAllByUser(@PathVariable("userId") Long userId, @RequestBody PageInfo pageInfo){
//        PageRequest pageRequest = PageRequest.of(pageInfo.getIndex(), pageInfo.getSize());
//        return postService.findAllByUserId(userId,pageRequest);
//    }
//
//    @PostMapping("/all/me")
//    public Page<PostResponse> getAllByMe(@RequestBody PageInfo pageInfo){
//        PageRequest pageRequest = PageRequest.of(pageInfo.getIndex(), pageInfo.getSize());
//        return postService.findAllByUserId(profile.getId(),pageRequest);
//    }
//
//    @PostMapping("/search")
//    public Page<PostResponse> search(@RequestBody PostFilterRequest postFilterRequest){
//        return postService.search(postFilterRequest);
//    }
}
