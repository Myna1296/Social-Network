package com.example.websocialnetwork.controller;
import com.example.websocialnetwork.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class MainController {

    @GetMapping("/")
    public String indexPage(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        model.addAttribute("user", new UserDTO());
        return "index";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        String email = userDTO.getEmail();
       // UserDTO userByEmail = userService.getUserByEmail(email);
        if (email == null) {
            model.addAttribute("registrationError", true);
            model.addAttribute("user", userDTO);
            return "index";
        }
        //userService.createUser(userDTO);
        return "registration-confirmation";
    }

}
