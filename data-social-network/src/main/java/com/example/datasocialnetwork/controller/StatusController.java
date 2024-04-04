package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.NewStatusRequest;
import com.example.datasocialnetwork.dto.request.StatusRequest;
import com.example.datasocialnetwork.dto.request.UpdateStatusRequest;
import com.example.datasocialnetwork.service.impl.StatusServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/add")
    public  ResponseEntity<?> saveStatus( MultipartHttpServletRequest request){
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        // Lấy tệp MultipartFile từ tên trường "image"
        MultipartFile file = request.getFile("image");
        NewStatusRequest newStatusRequest = new NewStatusRequest();
        newStatusRequest.setImage(file);
        newStatusRequest.setContent(content);
        newStatusRequest.setTitle(title);
        return statusService.addNewStatus(newStatusRequest);
    }
    @PutMapping("/update") // update post
    public ResponseEntity<?> updateStatus(MultipartHttpServletRequest request){
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        // Lấy tệp MultipartFile từ tên trường "image"
        MultipartFile file = request.getFile("image");
        String id = request.getParameter("id");
        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setImage(file);
        updateStatusRequest.setContent(content);
        updateStatusRequest.setTitle(title);
        updateStatusRequest.setId(Long.parseLong(id));
        return statusService.updateStatus(updateStatusRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStatus(@PathVariable("id") Long postId){
    return statusService.deleteStatus(postId);
}

    @PostMapping()
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
