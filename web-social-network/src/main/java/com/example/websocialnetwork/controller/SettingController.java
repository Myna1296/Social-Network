package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.PasswordChangeDTO;
import com.example.websocialnetwork.dto.UserDTO;
import com.example.websocialnetwork.dto.reponse.ResponseOk;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.*;
import static com.example.websocialnetwork.util.Validation.checkBirthday;
import static com.example.websocialnetwork.util.Validation.checkUserName;

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
    public String getSettingsPage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<UserInfo> responseEntity = restTemplate.exchange(
                    path + API_USER_INFO,
                    HttpMethod.GET,
                    requestEntity,
                    UserInfo.class,
                    request.getSession().getAttribute("email")
            );
            UserInfo userInfo = responseEntity.getBody();
            if (userInfo == null) {
                model.addAttribute("message", MESS_001);
                return VIEW_ERR;
            }
//            if (userInfo.getError() != null) {
//                model.addAttribute("message", userInfo.getError());
//                return VIEW_ERR;
//            }
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
            return "settings";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
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
        UserInfo user = getUserFromSession(request);
        byte[] bytes = multipartFile.getBytes();
        Path pathImages = getProfileImagesPath();
        try {
            if(user.getAvata() !=null) {
                if (Files.exists(pathImages.resolve(user.getAvata()))) {
                    Files.delete(pathImages.resolve(user.getAvata()));
                }
            }
            String newFileName = getNewFileName(user.getUserName(), multipartFile);
            Files.write(pathImages.resolve(newFileName), bytes);
            user.setAvata(newFileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<UserInfo> requestEntity = new HttpEntity<>(user,headers);
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_UPDATE_IMAGE,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if (responseBody == null) {
                return VIEW_ERROR;
            } else if (responseBody.getCode() == 1) {
                Files.delete(pathImages.resolve(user.getAvata()));
                model.addAttribute("imageError", responseBody.getMessage());
                return "settings";
            }
            return "redirect:/user/settings";
        } catch (IOException ex) {
            model.addAttribute("imageError", "Error copying the image");
            return "settings";
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }

    @PostMapping("/settings")
    public String updateProfileUser(@ModelAttribute("user") UserInfo userInfo,
                                    Model model,HttpServletRequest request )  {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        String checkBirthDay = checkBirthday(userInfo.getBirthday());
        if(checkBirthDay != null) {
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
            model.addAttribute("passwordError", "");
            model.addAttribute("updateError", true);
            model.addAttribute("error", checkBirthDay);
            return "settings";
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
            HttpEntity<UserInfo> requestEntity = new HttpEntity<>(userInfo,headers);
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_UPDATE_PROFILE,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if (responseBody == null) {
                return VIEW_ERROR;
            } else if (responseBody.getCode() == 1) {
                model.addAttribute("updateError", true);
                model.addAttribute("error", responseBody.getMessage());
                model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
                return "settings";
            }
            return "redirect:/user/settings";
        } catch (Exception e) {
            model.addAttribute("message", e);
            return VIEW_ERR;
        }
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid @ModelAttribute("passwordChangeDTO") PasswordChangeDTO passwordChangeDTO,
                                 BindingResult bindingResult, HttpServletRequest request, Model model) {
        if(bindingResult.hasErrors()) {
            return "settings";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + request.getSession().getAttribute("token"));
        HttpEntity<PasswordChangeDTO> requestEntity = new HttpEntity<>(passwordChangeDTO,headers);
        try {
            //call API
            ResponseEntity<ResponseOk> response = restTemplate.exchange(
                    path + API_UPDATE_PASSWORD,
                    HttpMethod.POST,
                    requestEntity,
                    ResponseOk.class
            );
            ResponseOk responseBody = response.getBody();
            if ( responseBody == null){
                return VIEW_ERROR;
            }else if (responseBody.getCode() == 1) {
                model.addAttribute("passwordError", true);
                model.addAttribute("error", responseBody.getMessage());
                model.addAttribute("passwordChangeDTO", passwordChangeDTO);
                return "settings";
            }
            return "settings";
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                model.addAttribute("passwordError", true);
                model.addAttribute("error", "Invalid data submitted");
                model.addAttribute("passwordChangeDTO", passwordChangeDTO);
                return "settings";
            } else {
                return VIEW_ERROR;
            }
        }catch (Exception e) {
            return VIEW_ERROR;
        }
    }
}
