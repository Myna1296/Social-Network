package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.LoginDTO;
import com.example.websocialnetwork.dto.reponse.UserInfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.imageio.IIOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.websocialnetwork.common.Const.*;

@Controller
@RequestMapping("/user")
public class UserController {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${api.path}")
    private String path;

    @GetMapping("/profile")
    public String getProfilePage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("email") == null) {
            response.sendRedirect(request.getContextPath());
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserInfoResponse> responseEntity = restTemplate.exchange(
                    path + API_USER_INFO,
                    HttpMethod.GET,
                    requestEntity,
                    UserInfoResponse.class,
                    request.getSession().getAttribute("email")
            );
            UserInfoResponse userInfo = responseEntity.getBody();
            if (userInfo.getError() != null){
                model.addAttribute("message", userInfo.getError());
                return VIEW_ERR;
            }
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("user", userInfo);
            return "E007";
        } catch (Exception ex){
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }
}
