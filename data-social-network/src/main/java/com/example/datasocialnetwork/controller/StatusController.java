package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.NewStatusRequest;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.dto.request.UpdateStatusRequest;
import com.example.datasocialnetwork.service.impl.StatusServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@RestController
@RequestMapping("/api/status")
@AllArgsConstructor
public class StatusController {

    @Autowired
    private StatusServiceImpl  statusService;

    @PostMapping(value = "/add",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity<?> saveStatus( @RequestParam(value = "image", required = false) MultipartFile file, @RequestParam("title") String title,
                                          @RequestParam("content") String content){
        NewStatusRequest newStatusRequest = new NewStatusRequest();
        newStatusRequest.setImage(file);
        newStatusRequest.setContent(content);
        newStatusRequest.setTitle(title);
        return statusService.addNewStatus(newStatusRequest);
    }
    @PutMapping(value = "/update",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // update post
    public ResponseEntity<?> updateStatus( @RequestParam(value = "image" , required = false) MultipartFile file, @RequestParam("title") String title,
                                           @RequestParam("content") String content,@RequestParam("id") Long id ){
        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setImage(file);
        updateStatusRequest.setContent(content);
        updateStatusRequest.setTitle(title);
        updateStatusRequest.setId(id);
        return statusService.updateStatus(updateStatusRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable("id") Long postId){
    return statusService.deleteStatus(postId);
}

    @PostMapping("/get-of-user")
    public ResponseEntity<?> getStatusFriendUser(@RequestBody StatusRequest statusRequest){
        return statusService.getStatusByUserId(statusRequest);
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<?> getNewsFeed (@RequestParam(name = "pageIndex", defaultValue = "1") int pageIndex,
                                              @RequestParam(name = "pageSize", defaultValue = "5") int pageSize){
        return statusService.getNewsFeed(pageIndex, pageSize);
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> search(@PathVariable("id") Long id){
        return statusService.searchStatus(id);
    }
}
