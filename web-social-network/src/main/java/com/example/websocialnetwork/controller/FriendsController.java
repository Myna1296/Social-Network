package com.example.websocialnetwork.controller;
import com.example.websocialnetwork.dto.FriendRequestDTO;
import com.example.websocialnetwork.dto.PasswordChangeDTO;
import com.example.websocialnetwork.dto.reponse.FriendResponse;
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
import java.util.Map;
import java.util.Set;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;
import static com.example.websocialnetwork.util.Validation.calculateTotalPages;

@Controller
@RequestMapping("/user/friends")
@RequiredArgsConstructor
public class FriendsController {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Value("${api.path}")
    private String path;
    @GetMapping
    public String showFriendsPage(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        UserInfo user = getUserFromSession(request);
        FriendRequestDTO friendRequestDTO = new FriendRequestDTO();
        friendRequestDTO.setId(Long.parseLong(user.getId()));
        friendRequestDTO.setPage(1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<FriendRequestDTO> requestEntity = new HttpEntity<>(friendRequestDTO,headers);
        try {
            ResponseEntity<FriendResponse> responseEntity = restTemplate.exchange(
                    path + API_GET_FRIENF_OF_USER,
                    HttpMethod.POST,
                    requestEntity,
                    FriendResponse.class
            );
            FriendResponse friendResponse = responseEntity.getBody();
            if (friendResponse.getCode() == 1) {
                model.addAttribute("message", friendResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("friendsOfUser",friendResponse.getFriendData());
            model.addAttribute("friendsOfUserTotal", calculateTotalPages(friendResponse.getTotal()));
            model.addAttribute("friendsOfUserPage", 1);
            return "friends";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

}
