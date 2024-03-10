package com.example.websocialnetwork.controller;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;

@Controller
@RequestMapping("/user/friends")
@RequiredArgsConstructor
public class FriendsController {

    @GetMapping
    public String showFriendsPage(@RequestParam(value = "search", required = false) String search,
            Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        UserInfo user = getUserFromSession(request);

        Map<String, Set<UserDTO>> friends = friendsService.getFriends(user.getId(), search);
        model.addAttribute("usersNotAcceptedRequests", friends.get("usersNotAcceptedRequests"));
        model.addAttribute("notAcceptedRequestsToUser", friends.get("notAcceptedRequestsToUser"));
        model.addAttribute("friendsOfUser", friends.get("friendsOfUser"));
        return "friends";
    }

}
