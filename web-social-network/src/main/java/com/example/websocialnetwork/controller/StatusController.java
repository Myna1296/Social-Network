package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.common.Const;
import com.example.websocialnetwork.dto.*;
import com.example.websocialnetwork.dto.reponse.*;
import com.example.websocialnetwork.dto.request.NewStatusRequest;
import com.example.websocialnetwork.dto.request.StatusInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.*;
import static com.example.websocialnetwork.util.ServerUtils.getProfileImagesPath;

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
            ResponseEntity<ResponseOk> responseEntity = restTemplate.exchange(
                    path + API_DELETE_STATUS,
                    HttpMethod.DELETE,
                    requestEntity,
                    ResponseOk.class,
                    id
            );
            ResponseOk responseOk = responseEntity.getBody();
            if (responseOk.getCode() == 1) {
                model.addAttribute("message", responseOk.getMessage());
                return VIEW_ERR;
            }
            UserInfo user = getUserFromSession(request);
            String path = "redirect:/user/status/" + user.getId();
            return path;

        } catch (Exception ex) {
            model.addAttribute("message", ex);
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
            ResponseEntity<StatusInfoResponse> responseEntity = restTemplate.exchange(
                    path + API_SEARCH_STATUS,
                    HttpMethod.GET,
                    requestEntity,
                    StatusInfoResponse.class,
                    id
            );
            StatusInfoResponse response = responseEntity.getBody();
            if (response.getCode() == 1) {
                model.addAttribute("message", response.getMessage());
                return VIEW_ERR;
            }
            StatusDTO statusDTO = response.getStatus();
            model.addAttribute("status", statusDTO);
            model.addAttribute("isLike", response.isLike());
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setStatusId(id);
            commentRequest.setPage(page);

            HttpEntity<CommentRequest> requestCommentEntity = new HttpEntity<>(commentRequest,headers);
            ResponseEntity<CommentListResponse> responseCommentEntity = restTemplate.exchange(
                    path + API_SEARCH_COMMENT,
                    HttpMethod.POST,
                    requestCommentEntity,
                    CommentListResponse.class
            );
            CommentListResponse commentListResponse = responseCommentEntity.getBody();
            if (commentListResponse.getCode() == 1) {
                model.addAttribute("message", commentListResponse.getMessage());
                return VIEW_ERR;
            }
            model.addAttribute("listComment", commentListResponse.getListComment());
            model.addAttribute("totalPage", commentListResponse.getTotalPage());
            model.addAttribute("page", commentListResponse.getPage());
            model.addAttribute("idUser", Long.parseLong(user.getId()));
            return "status-info";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
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
            ResponseEntity<StatusInfoResponse> responseEntity = restTemplate.exchange(
                    path + API_SEARCH_STATUS,
                    HttpMethod.GET,
                    requestEntity,
                    StatusInfoResponse.class,
                    id
            );
            StatusInfoResponse response = responseEntity.getBody();
            if (response.getCode() == 1) {
                model.addAttribute("message", response.getMessage());
                return VIEW_ERR;
            }
            StatusDTO statusDTO = response.getStatus();
            model.addAttribute("status", statusDTO);
            model.addAttribute("isEdit", "true");
            return "status-edit";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }


    @PostMapping("/add")
    public String addNewStatus(@RequestParam("imageStatus") MultipartFile imageFile,
                               @Valid @ModelAttribute("status") StatusDTO statusDTO,
                               BindingResult bindingResult, Model model,
                               HttpServletRequest request) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "status-edit";
        }

        String contentType = imageFile.getContentType();
        if(!allowedExtensions.contains(contentType)) {
            model.addAttribute("error","File extension is not supported");
            return "status-edit";
        }

        NewStatusRequest status = new NewStatusRequest();
        status.setTitle(statusDTO.getTitle());
        status.setContent(statusDTO.getContent());
        status.setImage(imageFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<StatusDTO> requestEntity = new HttpEntity<>(status, headers);
        try {
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_ADD_STATUS,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if ( responseBody == null){
                Files.delete(pathImages.resolve(newFileName));
                return VIEW_ERROR;
            }else if (responseBody.getCode() == 1) {
                Files.delete(pathImages.resolve(newFileName));
                model.addAttribute("error", responseBody.getMessage());
                model.addAttribute("status", statusDTO);
                return "status-edit";
            }
            String path = "redirect:/user/status/" + user.getId();
            return path;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                Files.delete(pathImages.resolve(newFileName));
                model.addAttribute("error", "Invalid data submitted");
                model.addAttribute("status", statusDTO);
                return "status-edit";
            } else {
                model.addAttribute("message", e);
                return VIEW_ERR;
            }
        }catch (Exception e) {
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
        String contentType = imageFile.getContentType();
        if(!allowedExtensions.contains(contentType)) {
            statusDTO.setStatusImage(imageFile.getOriginalFilename());
            model.addAttribute("status", statusDTO);
            model.addAttribute("error","File extension is not supported");
            return "status-edit";
        }
        UserInfo user = getUserFromSession(request);
        byte[] bytes = imageFile.getBytes();
        Path pathImages = getProfileImagesPath();
        String newFileName = getNewFileName("status-" +
                user.getUserName()+ "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")), imageFile);
        Files.write(pathImages.resolve(newFileName), bytes);
        StatusDTO status = new StatusDTO();
        status.setTitle(statusDTO.getTitle());
        status.setContent(statusDTO.getContent());
        status.setStatusImage(newFileName);
        status.setUserId(Long.parseLong(user.getId()));
        status.setId(statusDTO.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<StatusDTO> requestEntity = new HttpEntity<>(status, headers);
        try {
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_UPDATE_STATUS,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if ( responseBody == null){
                Files.delete(pathImages.resolve(newFileName));
                return VIEW_ERROR;
            }else if (responseBody.getCode() == 1) {
                Files.delete(pathImages.resolve(newFileName));
                model.addAttribute("error", responseBody.getMessage());
                statusDTO.setStatusImage(imageFile.getOriginalFilename());
                model.addAttribute("status", statusDTO);
                return "status-edit";
            }
            String path = "redirect:/user/status/" + user.getId();
            return path;
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                Files.delete(pathImages.resolve(newFileName));
                model.addAttribute("error", "Invalid data submitted");
                statusDTO.setStatusImage(imageFile.getOriginalFilename());
                model.addAttribute("status", statusDTO);
                return "status-edit";
            } else {
                model.addAttribute("message", e);
                return VIEW_ERR;
            }
        }catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }
}
