package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.LoginDTO;
import com.example.websocialnetwork.dto.UserDTO;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
        model.addAttribute("loginError", true);
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
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
        try {
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_REGISTER,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if (responseBody.getCode() == 1) {
                model.addAttribute("registrationError", true);
                model.addAttribute("error", responseBody.getMessage());
                model.addAttribute("user", userDTO);
                return VIEW_LOGIN;
            }
            return VIEW_COMFIRM_REGISTER;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("registrationError", true);
                model.addAttribute("error", "Invalid data submitted");
                model.addAttribute("user", userDTO);
                return VIEW_LOGIN;
            } else {
                return VIEW_ERROR;
            }
        }catch (Exception e) {
                return VIEW_ERROR;
        }
    }

    /*
    Handles user login
    Create by NgaPLT 2024/03/06
    <param name="user">UserDTO</param>
    <returns></returns>
     */
    @PostMapping("/login")
    public String loginUser(@RequestBody LoginDTO userLogin, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<LoginDTO> requestEntity = new HttpEntity<>(userLogin, headers);
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_LOGIN,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if (responseBody.getCode() == 1) {
                model.addAttribute("loginError", true);
                model.addAttribute("error", responseBody.getMessage());
                model.addAttribute("user", new UserDTO());
                return indexPage(null, null, model);
            }
            return VIEW_COMFIRM_REGISTER;

        }catch (Exception e) {
        return VIEW_ERROR;
        }

    }

}
