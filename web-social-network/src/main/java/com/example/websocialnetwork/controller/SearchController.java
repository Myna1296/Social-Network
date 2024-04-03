package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.reponse.SearchResponse;
import com.example.websocialnetwork.dto.request.SearchRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;


/**
 * Created by Dmitrii on 03.10.2019.
 */
@Controller
@RequestMapping("/user")
public class SearchController {


    @Value("${api.path}")
    private String path;

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("/search")
    public String getUserList(HttpServletRequest request,
                              @RequestParam(value = "search", required = false) String search,
                              @RequestParam(value = "page", required = false) Integer page,
                              Model model) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if(search == null){
            model.addAttribute("totalPage", 0);
            return "search";
        }
        if (page == null){
            page = 1;
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setPageIndex(page);
        searchRequest.setSearchValue(search);
        searchRequest.setPageSize(PAGE_SIZE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<SearchRequest> requestEntity = new HttpEntity<>(searchRequest, headers);

        try {
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_SEARCH_USER,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            ObjectMapper objectMapper = new ObjectMapper();
            SearchResponse searchResponse = objectMapper.readValue((String) response.getBody(), SearchResponse.class);

            model.addAttribute("search", search);
            model.addAttribute("listUser", searchResponse.getListUser());
            model.addAttribute("totalPage", searchResponse.getTotalPage());
            model.addAttribute("page", page);
            return "search";
        }catch (HttpClientErrorException e) {
            model.addAttribute("message", e.getResponseBodyAsString());
            return VIEW_ERR;

        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }
}
