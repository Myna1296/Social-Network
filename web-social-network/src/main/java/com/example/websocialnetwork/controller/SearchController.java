package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.SearchUserRequestDTO;
import com.example.websocialnetwork.dto.reponse.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        SearchUserRequestDTO searchUserRequestDTO = new SearchUserRequestDTO();
        searchUserRequestDTO.setPage(page);
        searchUserRequestDTO.setSearch(search);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<SearchUserRequestDTO> requestEntity = new HttpEntity<>(searchUserRequestDTO, headers);

        try {
            ResponseEntity<SearchResponse> response = restTemplate.exchange(
                    path + API_SEARCH_USER,
                    HttpMethod.POST,
                    requestEntity,
                    SearchResponse.class
            );
            SearchResponse searchResponse = response.getBody();
            if (searchResponse == null) {
                model.addAttribute("message", MESS_001);
                return VIEW_ERR;
            }
            if (searchResponse.getCode() != 0) {
                model.addAttribute("message", searchResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("search", search);
            model.addAttribute("listUser", searchResponse.getListUser());
            model.addAttribute("totalPage", searchResponse.getTotalPage());
            model.addAttribute("page", page);
            return "search";
        } catch(Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }
}
