package com.example.websocialnetwork.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.example.websocialnetwork.common.Const.*;

@Controller
@RequestMapping("/user/like")
public class LikeController {
    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Value("${api.path}")
    private String path;

    @GetMapping("/add/{id}")
    public String addLike(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<String> requestEntity = new HttpEntity<>( headers);
            //call API
            restTemplate.exchange(
                    path + API_ADD_LIKE,
                    HttpMethod.POST,
                    requestEntity,
                    String.class,
                    id
            );
            String path =  "redirect:/user/status/info/" + id;
            return path;
        }catch (HttpClientErrorException ex) {
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

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id,
                                Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(
                    path + API_DELETE_LIKE,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class,
                    id
            );
            String path =  "redirect:/user/status/info/" + id;
            return path;

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
