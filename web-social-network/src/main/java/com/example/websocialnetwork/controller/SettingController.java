package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.PasswordChangeDTO;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Set;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.Validation.checkBirthday;

@Controller
@RequestMapping("/user")
public class SettingController {
    @Value("#{'${allowed.file.types}'.split(',')}")
    private Set<String> allowedExtensions;

    @Value("${api.path}")
    private String path;

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @GetMapping("/settings")
    public String getSettingsPage(Model model, HttpServletRequest request, @ModelAttribute(value = "updateSuccess") String mess){
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_USER_INFO,
                    HttpMethod.GET,
                    requestEntity,
                    String.class
            );

            ObjectMapper objectMapper = new ObjectMapper();
            UserInfo userInfo = objectMapper.readValue((String) response.getBody(), UserInfo.class);
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
            model.addAttribute("updateSuccess", mess );
            return "settings";
        }catch (HttpClientErrorException e) {
            HttpStatus statusCode = e.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", e.getResponseBodyAsString());
                return VIEW_ERR;
            }
            model.addAttribute("message", e.getResponseBodyAsString());
            return VIEW_ERR;

        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }

    @PostMapping("/uploadImage")
    public String uploadImage(MultipartHttpServletRequest request ,Model model) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        MultipartFile multipartFile = request.getFile("imagefile");
        String contentType = multipartFile.getContentType();
        if(!allowedExtensions.contains(contentType)) {
            model.addAttribute("imageError","File extension is not supported");
            return "settings";
        }
        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            // Thêm tệp vào MultiValueMap
            body.add("image", multipartFile.getResource());

            // Thiết lập các header cho yêu cầu
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));

            // Tạo một HttpEntity với body và headers
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            restTemplate.exchange(
                    path + API_UPDATE_IMAGE,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );
            return "redirect:/user/settings";
        } catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
                model.addAttribute("passwordError", false);
                model.addAttribute("updateError", true);
                model.addAttribute("error", ex.getResponseBodyAsString());
                return "settings";
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }

    @PostMapping("/settings")
    public String updateProfileUser(@ModelAttribute("user") UserInfo userInfo,
                                    Model model,HttpServletRequest request,  RedirectAttributes attributes )  {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        String checkBirthDay = checkBirthday(userInfo.getBirthday());
        if(checkBirthDay != null) {
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
            model.addAttribute("passwordError", false);
            model.addAttribute("updateError", true);
            model.addAttribute("error", checkBirthDay);
            return "settings";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<UserInfo> requestEntity = new HttpEntity<>(userInfo,headers);
            ResponseEntity<?> response =  restTemplate.exchange(
                    path + API_UPDATE_PROFILE,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );
            attributes.addFlashAttribute("updateSuccess",  response.getBody());
            return "redirect:/user/settings";
        }  catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
                model.addAttribute("passwordError", false);
                model.addAttribute("updateError", true);
                model.addAttribute("error", ex.getResponseBodyAsString());
                return "settings";
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("passwordChangeDTO") PasswordChangeDTO passwordChangeDTO,
                                 BindingResult bindingResult, HttpServletRequest request, Model model,
                                 RedirectAttributes attributes) {
        if(bindingResult.hasErrors()) {
            return "settings";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<PasswordChangeDTO> requestEntity = new HttpEntity<>(passwordChangeDTO,headers);
        try {
            //call API
            ResponseEntity<?> response = restTemplate.exchange(
                    path + API_UPDATE_PASSWORD,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );
            attributes.addFlashAttribute("updateSuccess",  response.getBody());
            return "redirect:/user/settings";
        } catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            if( statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
                model.addAttribute("passwordChangeDTO", passwordChangeDTO);
                model.addAttribute("passwordError",  true);
                model.addAttribute("updateError", false);
                model.addAttribute("error",ex.getResponseBodyAsString());
                return "settings";
            }
            model.addAttribute("message", ex.getResponseBodyAsString());
            return VIEW_ERR;
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }
}
