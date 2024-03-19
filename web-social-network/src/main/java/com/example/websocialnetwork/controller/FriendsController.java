package com.example.websocialnetwork.controller;
import com.example.websocialnetwork.dto.FriendRequestDTO;
import com.example.websocialnetwork.dto.FriendShipRequestDTO;
import com.example.websocialnetwork.dto.PasswordChangeDTO;
import com.example.websocialnetwork.dto.reponse.FriendResponse;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import com.example.websocialnetwork.exceptionHandling.BadRequestException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
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
    public String showFriendsPage(Model model, HttpServletRequest request,
                                  @RequestParam(value = "index", required = false) Integer index,
                                  @RequestParam(value = "page", required = false) Integer page) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        int pageFriendsOfUser = 1;
        int pageUsersNotAcceptedRequests = 1;
        int pageNotAcceptedRequestsToUser = 1;
        if( index != null) {
            if (index == 1) {
                pageUsersNotAcceptedRequests = page == null ? pageUsersNotAcceptedRequests : page;
                pageFriendsOfUser = page == null ? pageFriendsOfUser : page;
            } else if (index == 2) {
                pageNotAcceptedRequestsToUser = page == null ? pageNotAcceptedRequestsToUser : page;
            } else if (index == 3) {
                pageFriendsOfUser = page == null ? pageFriendsOfUser : page;
            }
        }

        UserInfo user = getUserFromSession(request);
        FriendRequestDTO friendsOfUserRequest = new FriendRequestDTO(Long.parseLong(user.getId()), pageFriendsOfUser );
        FriendRequestDTO usersNotAcceptedRequests = new FriendRequestDTO(Long.parseLong(user.getId()), pageUsersNotAcceptedRequests );
        FriendRequestDTO notAcceptedRequestsToUser = new FriendRequestDTO(Long.parseLong(user.getId()), pageNotAcceptedRequestsToUser );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<FriendRequestDTO> requestfriendsOfUserRequest = new HttpEntity<>(friendsOfUserRequest,headers);
        try {
            ResponseEntity<FriendResponse> responseEntity = restTemplate.exchange(
                    path + API_GET_FRIENF_OF_USER,
                    HttpMethod.POST,
                    requestfriendsOfUserRequest,
                    FriendResponse.class
            );
            FriendResponse friendResponse = responseEntity.getBody();
            if (friendResponse.getCode() == 1) {
                model.addAttribute("message", friendResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("friendsOfUser",friendResponse.getFriendData());
            model.addAttribute("friendsOfUserTotal", calculateTotalPages(friendResponse.getTotal()));
            model.addAttribute("friendsOfUserPage", pageFriendsOfUser);

            HttpEntity<FriendRequestDTO> requestusersNotAccepted = new HttpEntity<>(usersNotAcceptedRequests,headers);
            ResponseEntity<FriendResponse> usersNotAcceptedEntity = restTemplate.exchange(
                    path + API_GET_REQUEST_USER_NOT_ACCEPTE,
                    HttpMethod.POST,
                    requestusersNotAccepted,
                    FriendResponse.class
            );
            FriendResponse usersNotAcceptedResponse = usersNotAcceptedEntity.getBody();
            if (friendResponse.getCode() == 1) {
                model.addAttribute("message", friendResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("usersNotAcceptedRequests",usersNotAcceptedResponse.getFriendData());
            model.addAttribute("usersNotAcceptedRequestsTotal", calculateTotalPages(usersNotAcceptedResponse.getTotal()));
            model.addAttribute("usersNotAcceptedRequestsPage", pageUsersNotAcceptedRequests);

            HttpEntity<FriendRequestDTO> requestNotAcceptedToUser = new HttpEntity<>(notAcceptedRequestsToUser,headers);
            ResponseEntity<FriendResponse> notAcceptedRequestsToUserEntity = restTemplate.exchange(
                    path + API_GET_REQUEST_NOT_ACCEPTE_TO_USER,
                    HttpMethod.POST,
                    requestNotAcceptedToUser,
                    FriendResponse.class
            );
            FriendResponse notAcceptedRequestsToUserResponse = notAcceptedRequestsToUserEntity.getBody();
            if (friendResponse.getCode() == 1) {
                model.addAttribute("message", friendResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("notAcceptedRequestsToUser",notAcceptedRequestsToUserResponse.getFriendData());
            model.addAttribute("notAcceptedRequestsToUserTotal", calculateTotalPages(notAcceptedRequestsToUserResponse.getTotal()));
            model.addAttribute("notAcceptedRequestsToUserPage", pageNotAcceptedRequestsToUser);
            return "friends";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @GetMapping("/addToFriends/{friendId}")
    public String addToFriends(@PathVariable Long friendId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<ResponseOk> responseEntity = restTemplate.exchange(
                    path + API_ADD_FRIEND,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class,
                    friendId
            );
            ResponseOk responseOk = responseEntity.getBody();
            redirectAttributes.addFlashAttribute("message", responseOk.getMessage());
                return "redirect:/user/profile/"+ friendId;
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @GetMapping("/delete")
    public String deleteFriendship( @RequestParam(value = "index") Integer index,
                                    @RequestParam(value = "id") Long id, HttpServletRequest request, Model model) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        if( index == null || (index != 1 & index != 2 & index != 3)){
            return "redirect:/user/friends";
        }
        UserInfo user = getUserFromSession(request);
        friendShipRequestDTO.setId(id);
        if( index == 3){
            friendShipRequestDTO.setAccepte(true);
        } else{
            friendShipRequestDTO.setAccepte(false);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<FriendShipRequestDTO> requestEntity = new HttpEntity<>(friendShipRequestDTO,headers);
        try {
            ResponseEntity<ResponseOk> responseEntity = restTemplate.exchange(
                    path + API_DELETE_FRIENDSHIP,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseOk = responseEntity.getBody();
            if(responseOk.getCode() != 0){
                model.addAttribute("message", responseOk.getMessage());
                return VIEW_ERR;
            }
            return "redirect:/user/friends";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @GetMapping("/accepte")
    public String accepteFriends(@RequestParam(value = "id") Long id, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        FriendShipRequestDTO friendShipRequestDTO = new FriendShipRequestDTO();
        friendShipRequestDTO.setId(id);
        friendShipRequestDTO.setAccepte(false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<FriendShipRequestDTO> requestEntity = new HttpEntity<>(friendShipRequestDTO,headers);
        try {
            ResponseEntity<ResponseOk> responseEntity = restTemplate.exchange(
                    path + API_ACCEPTE_FRIENDSHIP,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseOk = responseEntity.getBody();
            if(responseOk.getCode() != 0){
                model.addAttribute("message", responseOk.getMessage());
                return VIEW_ERR;
            }
            return "redirect:/user/friends";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

}
