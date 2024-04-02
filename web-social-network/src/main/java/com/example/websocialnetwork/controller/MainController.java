package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.request.ComfirmOTPRequest;
import com.example.websocialnetwork.dto.request.LoginRequest;
import com.example.websocialnetwork.dto.request.RegisterUserRequest;
import com.example.websocialnetwork.dto.request.RegisterUserRequestView;
import lombok.RequiredArgsConstructor;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Value("${api.path}")
    private String path;

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @GetMapping(value = {"/", "/login"})
    public String indexPage(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        if (request.getSession().getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/user/profile");
            return null;
        }
        model.addAttribute("user", new RegisterUserRequestView());
        return VIEW_LOGIN;
    }
    /*
    Handles user registration
    Create by NgaPLT 2024/03/06
    <param name="user">UserDTO</param>
    <returns></returns>
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") RegisterUserRequestView user, BindingResult bindingResult,
                               Model model) {
        //userDTO validator
        //Check if there are any errors during the validator process.
        if (bindingResult.hasErrors()) {
            return VIEW_LOGIN;
        }
        RegisterUserRequest userRequest = new RegisterUserRequest();
        userRequest.setEmail(user.getEmail());
        userRequest.setPassword(user.getPassword());
        userRequest.setUserName(user.getUserName());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<RegisterUserRequest> requestEntity = new HttpEntity<>(userRequest, headers);
        try {
            //call API
            restTemplate.exchange(
                    path + API_REGISTER,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            return VIEW_COMFIRM_REGISTER;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("registrationError", true);
                model.addAttribute("error", e.getResponseBodyAsString());
                model.addAttribute("user", user);
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
    public String loginUser(@RequestParam("email") String email,@RequestParam("password") String password, Model model) {
        try {
            LoginRequest userLogin = new LoginRequest();
            userLogin.setEmail(email);
            userLogin.setPassword(password);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<LoginRequest> requestEntity = new HttpEntity<>(userLogin, headers);
            //call API
            restTemplate.exchange(
                    path + API_LOGIN,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            model.addAttribute("email", email);
            return VIEW_COMFIRM_OTP;

        } catch (HttpClientErrorException e) {

            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND){
                model.addAttribute("loginError", true);
                model.addAttribute("error", e.getResponseBodyAsString());
                model.addAttribute("user", new RegisterUserRequestView());
                return VIEW_LOGIN;
            }
            return VIEW_ERROR;
        }catch (Exception e) {
                return VIEW_ERROR;
        }
    }

    @PostMapping("/comfirmOTP")
    public String comfirmOTPLogin(@RequestParam("email") String email,@RequestParam("otp") String otp, Model model,
                                  HttpServletRequest request, HttpServletResponse responseHttp) {
        try {
            ComfirmOTPRequest otpComfirm = new ComfirmOTPRequest();
            otpComfirm.setEmail(email);
            otpComfirm.setOtp(otp);
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<ComfirmOTPRequest> requestEntity = new HttpEntity<>(otpComfirm, headers);
            //call API
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_COMFIRM_OTP_LOGIN,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            HttpHeaders headersResponse = response.getHeaders();
            String token = headersResponse.getFirst("Authorization");
            request.getSession().setAttribute("token", token.substring(7));
            request.getSession().setAttribute("email", email);
            responseHttp.sendRedirect(request.getContextPath() + "/user/profile");
            return null;

        }  catch (HttpClientErrorException e) {

            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND){
                model.addAttribute("comfirmOTPError", true);
                model.addAttribute("error", e.getResponseBodyAsString());
                model.addAttribute("email", email);
                return VIEW_COMFIRM_OTP;
            }
            return VIEW_ERROR;
        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            //call API
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_FORGOT_PASSWORD,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );
            model.addAttribute("loginError", true);
            model.addAttribute("error", response.getBody());
            model.addAttribute("user", new RegisterUserRequestView());
            return VIEW_LOGIN;

        }catch (HttpClientErrorException e) {

            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND){
                model.addAttribute("loginError", true);
                model.addAttribute("error", e.getResponseBodyAsString());
                model.addAttribute("user", new RegisterUserRequestView());
                return VIEW_LOGIN;
            }
            return VIEW_ERROR;
        }catch (Exception e) {
            return VIEW_ERROR;
        }

    }
}
