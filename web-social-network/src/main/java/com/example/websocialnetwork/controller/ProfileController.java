package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.reponse.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;

@Controller
@RequestMapping("/user")
public class ProfileController {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Value("${api.path}")
    private String path;

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserInfo> responseEntity = restTemplate.exchange(
                    path + API_USER_INFO,
                    HttpMethod.GET,
                    requestEntity,
                    UserInfo.class,
                    request.getSession().getAttribute("email")
            );
            UserInfo userInfo = responseEntity.getBody();
            if (userInfo == null) {
                model.addAttribute("message", MESS_001);
                return VIEW_ERR;
            }
            if (userInfo.getError() != null) {
                model.addAttribute("message", userInfo.getError());
                return VIEW_ERR;
            }
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("user", userInfo);
            return "E007";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @GetMapping("/profile/{id}")
    public String showUserPage(@PathVariable Long id, Model model, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        UserInfo userInfo = getUserFromSession(request);
        if ( userInfo == null) {
            response.sendRedirect(request.getContextPath());
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            return "E007";
    }
}
