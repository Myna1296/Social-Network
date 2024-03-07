package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import static com.example.websocialnetwork.common.Const.VIEW_LOGIN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

//    @GetMapping("/error")
//    public String handle404Error(Model model) {
//        model.addAttribute("user", new UserDTO());
//        return VIEW_LOGIN;
//    }
}
