package com.example.datasocialnetwork.controller;

import com.example.datasocialnetwork.dto.request.CommentRequest;
import com.example.datasocialnetwork.dto.request.NewCommentRequest;
import com.example.datasocialnetwork.dto.response.ErrorResponse;
import com.example.datasocialnetwork.service.impl.CommentServiceImpl;
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
@AllArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentServiceImpl commentService;

    @PostMapping()
    public ResponseEntity<?> getCommentByStatusId(@RequestBody CommentRequest commentRequest){
        return commentService.searchCommentByStatusId(commentRequest);
    }

    @PostMapping("/add")
    public  ResponseEntity<?> addNewComment(@RequestBody @Valid NewCommentRequest commentDTO, BindingResult bindingResult){
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
        return commentService.addNewCommen(commentDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id){
        return commentService.deleteCommnet(id);
    }
}
