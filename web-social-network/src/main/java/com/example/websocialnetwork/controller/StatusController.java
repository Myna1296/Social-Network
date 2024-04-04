package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.*;
import com.example.websocialnetwork.dto.reponse.*;
import com.example.websocialnetwork.dto.request.NewStatusRequest;
import com.example.websocialnetwork.dto.request.UpdateStatusRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.util.Set;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.*;

@Controller
@RequestMapping("/user/status")
@RequiredArgsConstructor
public class StatusController {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    @Value("${api.path}")
    private String path;

    @Value("#{'${allowed.file.types}'.split(',')}")
    private Set<String> allowedExtensions;

    @GetMapping
    public String showStatusPage( @RequestParam(value = "page", required = false) Integer page, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (page == null || page < 1){
            page = 1;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_NEWS_FEED,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    page,
                    PAGE_SIZE
            );
            ObjectMapper objectMapper = new ObjectMapper();
            StatusAllResponse statusAllResponse = objectMapper.readValue((String) response.getBody(), StatusAllResponse.class);
            model.addAttribute("statusList", statusAllResponse.getListStatus());
            model.addAttribute("totalPage", statusAllResponse.getTotalPage());
            model.addAttribute("page", page);
            return "status";

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

    @GetMapping("/{id}")
    public String showStatusUserPage(@PathVariable Long id,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (page == null || page < 1){
            page = 1;
        }
        StatusRequest statusRequest = new StatusRequest();
        statusRequest.setPageIndex(page);
        statusRequest.setUserId(id);
        statusRequest.setPageSize(PAGE_SIZE);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    path + API_USER_INFO_BY_ID,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    id
            );
            ObjectMapper objectMapper = new ObjectMapper();
            UserInfo userInfo = objectMapper.readValue((String) responseEntity.getBody(), UserInfo.class);

            HttpEntity<StatusRequest> requestStatus = new HttpEntity<>(statusRequest, headers);
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_GET_ALL_STATUS_USER,
                    HttpMethod.POST,
                    requestStatus,
                    String.class
            );

            StatusAllResponse statusAllResponse = objectMapper.readValue((String) response.getBody(), StatusAllResponse.class);
            UserInfo user = getUserFromSession(request);
            boolean isStatusUser = false;
            if ( userInfo.getId().equals(user.getId())){
                isStatusUser = true;
            }
            model.addAttribute("statusList", statusAllResponse.getListStatus());
            model.addAttribute("id", id);
            model.addAttribute("userName", userInfo.getUserName());
            model.addAttribute("totalPage", statusAllResponse.getTotalPage());
            model.addAttribute("page", page);
            model.addAttribute("isStatusUser", isStatusUser);
            return "status";

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

    @GetMapping("/delete/{id}")
    public String deleteStatus(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(
                    path + API_DELETE_STATUS,
                    HttpMethod.DELETE,
                    requestEntity,
                    String.class,
                    id
            );
            UserInfo user = getUserFromSession(request);
            String path = "redirect:/user/status/" + user.getId();
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

    @GetMapping("info/{id}")
    public String showStatusInfoPage(@PathVariable Long id,
                                     @RequestParam(value = "page", required = false) Integer page,
                                     Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (page == null || page < 1){
            page = 1;
        }
        UserInfo user = getUserFromSession(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    path + API_SEARCH_STATUS,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    id
            );
            ObjectMapper objectMapper = new ObjectMapper();
            StatusInfoResponse statusInfoResponse = objectMapper.readValue((String) responseEntity.getBody(), StatusInfoResponse.class);
            model.addAttribute("status", statusInfoResponse.getStatus());
            model.addAttribute("isLike", statusInfoResponse.isLike());
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setStatusId(id);
            commentRequest.setPageIndex(page);
            commentRequest.setPageSize(PAGE_SIZE);

            HttpEntity<CommentRequest> requestCommentEntity = new HttpEntity<>(commentRequest,headers);
            ResponseEntity<?> responseCommentEntity = restTemplate.exchange(
                    path + API_SEARCH_COMMENT,
                    HttpMethod.POST,
                    requestCommentEntity,
                    String.class
            );
            CommentListResponse commentListResponse =  objectMapper.readValue((String) responseCommentEntity.getBody(), CommentListResponse.class);

            model.addAttribute("listComment", commentListResponse.getListComment());
            model.addAttribute("totalPage", commentListResponse.getTotalPage());
            model.addAttribute("page", page);
            model.addAttribute("idUser", Long.parseLong(user.getId()));
            return "status-info";
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

    @GetMapping("/add")
    public String addStatusPage(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        StatusDTO statusDTO = new StatusDTO();
        model.addAttribute("status", statusDTO);
        model.addAttribute("isEdit", "false");;
        return "status-edit";
    }

    @GetMapping("/edit/{id}")
    public String editStatusPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> responseEntity = restTemplate.exchange(
                    path + API_SEARCH_STATUS,
                    HttpMethod.GET,
                    requestEntity,
                    String.class,
                    id
            );
            ObjectMapper objectMapper = new ObjectMapper();
            StatusInfoResponse statusInfoResponse = objectMapper.readValue((String) responseEntity.getBody(), StatusInfoResponse.class);
            model.addAttribute("status", statusInfoResponse.getStatus());
            model.addAttribute("isEdit", "true");
            return "status-edit";
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


    @PostMapping("/add")
    public String addNewStatus(@RequestParam("imageStatus") MultipartFile imageFile,
                               @Valid @ModelAttribute("status") StatusDTO statusDTO,
                               BindingResult bindingResult, Model model,
                               HttpServletRequest request)  {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "status-edit";
        }
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

        if(!imageFile.getOriginalFilename().isEmpty()) {
            String contentType = imageFile.getContentType();
            // Thêm tệp vào MultiValueMap
            formData.add("image", imageFile.getResource());
            if (!allowedExtensions.contains(contentType)) {
                model.addAttribute("error", "File extension is not supported");
                return "status-edit";
            }
        } else {
            formData.add("image",null);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));

        formData.add("title", statusDTO.getTitle());
        formData.add("content", statusDTO.getContent());

        // Tạo đối tượng HttpEntity chứa dữ liệu và tiêu đề
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        try {
            //call API
            restTemplate.exchange(
                    path + API_ADD_STATUS,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            UserInfo user = getUserFromSession(request);
            String path = "redirect:/user/status/" + user.getId();
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

    @PostMapping("/edit")
    public String editStatus(@RequestParam("imageStatus") MultipartFile imageFile,
                             @Valid @ModelAttribute("status") StatusDTO statusDTO,
                             BindingResult bindingResult, Model model,
                             HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "status-edit";
        }
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        if(!imageFile.getOriginalFilename().isEmpty()) {
            String contentType = imageFile.getContentType();
            // Thêm tệp vào MultiValueMap
            formData.add("image", imageFile.getResource());
            if (!allowedExtensions.contains(contentType)) {
                model.addAttribute("error", "File extension is not supported");
                return "status-edit";
            }
        } else {
            formData.add("image",null);
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));

        formData.add("title", statusDTO.getTitle());
        formData.add("content", statusDTO.getContent());
        formData.add("id", statusDTO.getId());

        // Tạo đối tượng HttpEntity chứa dữ liệu và tiêu đề
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(formData, headers);
        try {
            //call API
            restTemplate.exchange(
                    path + API_UPDATE_STATUS,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );
            UserInfo user = getUserFromSession(request);
            String path = "redirect:/user/status/" + user.getId();
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
