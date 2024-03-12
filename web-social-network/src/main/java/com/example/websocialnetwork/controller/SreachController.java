package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.reponse.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;


/**
 * Created by Dmitrii on 03.10.2019.
 */
@Controller
@RequestMapping("/user")
public class SreachController {

    @Value("${default.page.size}")
    private Integer defaultPageSize;


    @GetMapping("/sreach")
    public String getUserList(HttpServletRequest request,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "page", required = false) Integer page,
                              Model model) {
        UserInfo user = getUserFromSession(request);
        model.addAttribute("page", "");
        model.addAttribute("search", search);
        return "sreach";
    }
}
