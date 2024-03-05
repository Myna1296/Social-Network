package com.example.websocialnetwork.controller;
import com.example.websocialnetwork.dto.UserDTO;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import static com.example.websocialnetwork.common.Const.*;

@Controller
public class MainController {

    @Value("${api.path}")
    private String path;

    private static RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/")
    public String indexPage(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        model.addAttribute("user", new UserDTO());
        return VIEW_LOGIN;
    }
    /*
    Handles user registration
    Create by NgaPLT 2024/03/06
    <param name="user">UserDTO</param>
    <returns></returns>
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult,
                               Model model) {
        //userDTO validator
        //Check if there are any errors during the validator process.
        if (bindingResult.hasErrors()) {
            return VIEW_LOGIN;
        }
        try {
            ResponseOk response = restTemplate.postForObject(path + API_REGISTER, userDTO, ResponseOk.class);
            if (response.getCode() == 1) {
                model.addAttribute("registrationError", true);
                model.addAttribute("error", response.getMessage());
                model.addAttribute("user", userDTO);
                return VIEW_LOGIN;
            }
            //userService.createUser(userDTO);
            return VIEW_COMFIRM_REGISTER;
        }catch(HttpClientErrorException exception){
            return VIEW_ERROR;
        }
    }

}
