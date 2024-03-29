package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.StatusDTO;
import com.example.datasocialnetwork.dto.response.ErrorResponse;
import com.example.datasocialnetwork.service.impl.StatusServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/status")
@AllArgsConstructor
public class StatusController {

    @Autowired
    private StatusServiceImpl  statusService;
//    private final PostService postService;
//    private final Profile profile;
    @PostMapping("/add")
    public  ResponseEntity<?> saveStatus(@Valid @RequestBody StatusDTO statusDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrors(errors);
            errorResponse.setMessage("Invalid data submitted");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return statusService.addNewStatus(statusDTO);
    }
    @PostMapping("/update") // update post
    public ResponseEntity<?> updateStatus(@Valid @RequestBody StatusDTO statusDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrors(errors);
            errorResponse.setMessage("Invalid data submitted");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return statusService.updateStatus(statusDTO);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity deleteStatus(@PathVariable("postId") Long postId){
    return statusService.deleteStatus(postId);
}

    @GetMapping("/all/friend/{pageId}")
    public ResponseEntity<?> getStatusFriendUser(@PathVariable("pageId") Long pageId){
        return statusService.getStatusFriendUser(pageId);
    }

    @GetMapping("/all/{pageId}")
    public ResponseEntity<?> getPostByUserId (@PathVariable("pageId") Long pageId){
        return statusService.getStatusByUserId(pageId);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<?> search(@PathVariable("id") Long id){
        return statusService.searchStatus(id);
    }
}
