package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.request.NewCommentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


import static com.example.websocialnetwork.common.Const.*;

@Controller
@RequestMapping("/user/comment")
public class CommentController {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Value("${api.path}")
    private String path;

    @PostMapping("/add")
    public String addComment(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        try {
            NewCommentRequest comment = new NewCommentRequest();
            comment.setStatusId(Long.parseLong(request.getParameter("statusId")));
            comment.setContent(request.getParameter("content"));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<NewCommentRequest> requestEntity = new HttpEntity<>(comment, headers);
            //call API
            restTemplate.exchange(
                    path + API_ADD_COMMENT,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String path =  "redirect:/user/status/info/" + request.getParameter("statusId");
            return path;
        }catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", ex.getResponseBodyAsString());
                return VIEW_ERR;
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id, @RequestParam(value = "com")  Long comId,
                                Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(
                    path + API_DELETE_COMMENT,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class,
                    comId
            );

            String path =  "redirect:/user/status/info/" + id;
            return path;

        }catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", ex.getResponseBodyAsString());
                return VIEW_ERR;
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }
}
