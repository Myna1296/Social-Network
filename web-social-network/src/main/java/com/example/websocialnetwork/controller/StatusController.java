package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.FriendRequestDTO;
import com.example.websocialnetwork.dto.reponse.CommentResponse;
import com.example.websocialnetwork.dto.reponse.FriendResponse;
import com.example.websocialnetwork.dto.reponse.StatusResponse;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;
import static com.example.websocialnetwork.util.Validation.calculateTotalPages;

@Controller
@RequestMapping("/user/status")
@RequiredArgsConstructor
public class StatusController {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Value("${api.path}")
    private String path;

    @GetMapping
    public String showStatusPage(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        List<StatusResponse> statusList = new ArrayList<>();
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);
        statusList.add(status);
        statusList.add(status);
        model.addAttribute("statusList", statusList);
        return "status";
    }

    @GetMapping("/{id}")
    public String showStatusUserPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        List<StatusResponse> statusList = new ArrayList<>();
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);
        statusList.add(status);
        statusList.add(status);
        model.addAttribute("statusList", statusList);
        model.addAttribute("id", id);
        model.addAttribute("userName", "Test");
        return "status";
    }

    @GetMapping("info/{id}")
    public String showStatusInfoPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);

        List<CommentResponse> listComment = new ArrayList<>();
        CommentResponse comment = new CommentResponse();
        comment.setUserImage("image2.jpg");
        comment.setUserName("Test");
        comment.setText("Text1111");
        comment.setCreateDate("2023-02-02");
        listComment.add(comment);

        model.addAttribute("status", status);
        model.addAttribute("listComment", listComment);
        model.addAttribute("id", id);
        model.addAttribute("userName", "Test");
        return "status-info";
    }
}
