package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.CommentDTO;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;


import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;

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
            UserInfo user = getUserFromSession(request);
            CommentDTO comment = new CommentDTO();
            comment.setStatusId(Long.parseLong(request.getParameter("statusId")));
            comment.setContent(request.getParameter("content"));
            comment.setUserId(Long.parseLong(user.getId()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<CommentDTO> requestEntity = new HttpEntity<>(comment, headers);
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_ADD_COMMENT,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if ( responseBody == null){
                return VIEW_ERROR;
            }else if (responseBody.getCode() == 1) {
                model.addAttribute("message", responseBody.getMessage());
                return VIEW_ERR;
            }
            String path =  "redirect:/user/status/info/" + request.getParameter("statusId");
            return path;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("message", e);
                return VIEW_ERR;
            } else {
                model.addAttribute("message", e);
                return VIEW_ERR;
            }
        }catch (Exception e) {
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
            ResponseEntity<ResponseOk> responseEntity = restTemplate.exchange(
                    path + API_DELETE_COMMENT,
                    HttpMethod.GET,
                    requestEntity,
                    ResponseOk.class,
                    comId
            );
            ResponseOk responseOk = responseEntity.getBody();
            if (responseOk.getCode() == 1) {
                model.addAttribute("message", responseOk.getMessage());
                return VIEW_ERR;
            }
            String path =  "redirect:/user/status/info/" + id;
            return path;

        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }
}
