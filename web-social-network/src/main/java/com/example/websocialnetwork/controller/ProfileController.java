package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.reponse.CheckFriendShipResponse;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import com.example.websocialnetwork.dto.request.RegisterUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showProfilePage(@ModelAttribute(value = "exportMess") String mess, Model model, HttpServletRequest request){
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            //call API
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_USER_INFO,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            UserInfo userInfo = objectMapper.readValue((String) response.getBody(), UserInfo.class);
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("user",  userInfo);
            model.addAttribute("exportMess", mess );
            return "E007";
        }catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", e.getResponseBodyAsString());
                return VIEW_ERR;
            }
            model.addAttribute("message", e.getResponseBodyAsString());
            return VIEW_ERR;

        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }

    @GetMapping("/profile/{id}")
    public String showUserPage(@PathVariable Long id, Model model, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        UserInfo sessionUser = getUserFromSession(request);
        if(sessionUser.getId().equals(id)) {
            return "redirect:/user/profile";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserInfo> responseEntity = restTemplate.exchange(
                    path + API_USER_INFO_BY_ID,
                    HttpMethod.GET,
                    requestEntity,
                    UserInfo.class,
                    id
            );
            UserInfo userInfo = responseEntity.getBody();
            if (userInfo == null) {
                model.addAttribute("message", MESS_001);
                return VIEW_ERR;
            }
//            if (userInfo.getError() != null) {
//                model.addAttribute("message", userInfo.getError());
//                return VIEW_ERR;
//            }

            ResponseEntity<CheckFriendShipResponse> responseCheckFriendShip = restTemplate.exchange(
                    path + API_CHECK_FRIEND_SHIP,
                    HttpMethod.POST,
                    requestEntity,
                    CheckFriendShipResponse.class,
                    id
            );
            CheckFriendShipResponse checkFriendShipResponse = responseCheckFriendShip.getBody();
            if (checkFriendShipResponse == null) {
                model.addAttribute("message", MESS_001);
                return VIEW_ERR;
            }
//            if (checkFriendShipResponse.getCode() != 0) {
//                model.addAttribute("message", userInfo.getError());
//                return VIEW_ERR;
//            }
            model.addAttribute("sessionUser", sessionUser);
            model.addAttribute("usersHaveFriendship", checkFriendShipResponse.isCheckFriendShip());
            model.addAttribute("user", userInfo);
            return "E007";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @GetMapping("/export-file")
    public String exportFile (Model model, HttpServletRequest request, RedirectAttributes attributes) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    path + API_EXPORT_FILE,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );
            attributes.addFlashAttribute("exportMess",responseEntity.getBody());
            return "redirect:/user/profile";
        }catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", e.getResponseBodyAsString());
                return VIEW_ERR;
            }
            model.addAttribute("message", e.getResponseBodyAsString());
            return VIEW_ERR;

        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }
}
