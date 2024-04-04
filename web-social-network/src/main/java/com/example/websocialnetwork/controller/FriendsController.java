package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.FriendShipRequestDTO;
import com.example.websocialnetwork.dto.reponse.FriendResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
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


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>( headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    path + API_GET_FRIENF_OF_USER,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    pageFriendsOfUser,
                    PAGE_SIZE
            );
            ObjectMapper objectMapper = new ObjectMapper();
            FriendResponse friendResponse = objectMapper.readValue((String) responseEntity.getBody(), FriendResponse.class);

            model.addAttribute("friendsOfUser",friendResponse.getFriendData());
            model.addAttribute("friendsOfUserTotal", calculateTotalPages(friendResponse.getTotal()));
            model.addAttribute("friendsOfUserPage", pageFriendsOfUser);

            ResponseEntity<?> usersNotAcceptedEntity = restTemplate.exchange(
                    path + API_USER_SENT_FRIEND_REQUEST,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    pageUsersNotAcceptedRequests,PAGE_SIZE
            );
            FriendResponse usersNotAcceptedResponse = objectMapper.readValue((String) usersNotAcceptedEntity.getBody(), FriendResponse.class);
            model.addAttribute("usersNotAcceptedRequests",usersNotAcceptedResponse.getFriendData());
            model.addAttribute("usersNotAcceptedRequestsTotal", calculateTotalPages(usersNotAcceptedResponse.getTotal()));
            model.addAttribute("usersNotAcceptedRequestsPage", pageUsersNotAcceptedRequests);


            ResponseEntity<?> notAcceptedRequestsToUserEntity = restTemplate.exchange(
                    path + API_WAITING_USER_TO_ACCEPT,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    pageNotAcceptedRequestsToUser, PAGE_SIZE
            );
            FriendResponse notAcceptedRequestsToUserResponse = objectMapper.readValue((String) notAcceptedRequestsToUserEntity.getBody(), FriendResponse.class);
            model.addAttribute("notAcceptedRequestsToUser",notAcceptedRequestsToUserResponse.getFriendData());
            model.addAttribute("notAcceptedRequestsToUserTotal", calculateTotalPages(notAcceptedRequestsToUserResponse.getTotal()));
            model.addAttribute("notAcceptedRequestsToUserPage", pageNotAcceptedRequestsToUser);
            return "friends";
        } catch (HttpClientErrorException ex) {
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
            ResponseEntity<?> responseEntity =restTemplate.exchange(
                    path + API_ADD_FRIEND,
                    HttpMethod.POST,
                    requestEntity,
                    String.class,
                    friendId
            );
            redirectAttributes.addFlashAttribute("message", responseEntity.getBody());
            return "redirect:/user/profile/"+ friendId;
        } catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                redirectAttributes.addFlashAttribute("message", ex.getResponseBodyAsString());
                return "redirect:/user/profile/"+ friendId;
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
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
            restTemplate.exchange(
                    path + API_DELETE_FRIENDSHIP,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class
            );

            return "redirect:/user/friends";
        } catch (HttpClientErrorException ex) {
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

    @GetMapping("/accepte")
    public String accepteFriends(@RequestParam(value = "id") Long id, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(
                    path + API_ACCEPTE_FRIENDSHIP,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class,
                    id
            );

            return "redirect:/user/friends";
        } catch (HttpClientErrorException ex) {
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
