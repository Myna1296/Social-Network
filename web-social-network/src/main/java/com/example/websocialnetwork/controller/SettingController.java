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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;

@Controller
@RequestMapping("/user")
public class SettingController {
    @Value("#{'${allowed.file.types}'.split(',')}")
    private Set<String> allowedExtensions;

    @Value("${api.path}")
    private String path;

    @Value("${image.path}")
    private String pathImage;

    private static RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final CountDownLatch latch = new CountDownLatch(1);

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
            if (userInfo.getError() != null) {
                model.addAttribute("message", userInfo.getError());
                return VIEW_ERR;
            }
            request.getSession().setAttribute("user", userInfo);
            model.addAttribute("passwordChangeDTO", new PasswordChangeDTO());
            model.addAttribute("passwordError", "Passwords doesn't match");
            return "settings";
        } catch (Exception ex) {
            model.addAttribute("message", ex);
            return VIEW_ERR;
        }
    }

    @PostMapping("/uploadImage")
    public String uploadImage(MultipartHttpServletRequest request ,Model model) throws IOException {
        MultipartFile multipartFile = request.getFile("imagefile");
        String contentType = multipartFile.getContentType();
        if(!allowedExtensions.contains(contentType)) {
            model.addAttribute("imageError","File extension is not supported");
            return "settings";
        }
        String fileName = multipartFile.getOriginalFilename();
        UserInfo user = getUserFromSession(request);
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = user.getUserName() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileExtension;
        // Copy image
        try {
            Path destinationPath = Paths.get(pathImage, newFileName);
            Files.copy(multipartFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
            // Signal đến CountDownLatch rằng quá trình copy đã hoàn tất
            latch.countDown();
            user.setAvata(PROFILE_IMAGES + "/"+ newFileName );
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
                Files.deleteIfExists(Paths.get(pathImage, newFileName));
                return VIEW_ERROR;
            } else if (responseBody.getCode() == 1) {
                Files.deleteIfExists(Paths.get(pathImage, newFileName));
                model.addAttribute("imageError", responseBody.getMessage());
                return "settings";
            }
            latch.await();  // Đợi cho đến khi CountDownLatch được giảm xuống 0
            return "redirect:/user/settings";
        } catch (IOException e) {
            // Xử lý lỗi copy ảnh (nếu có)
            e.printStackTrace();
            model.addAttribute("imageError", "Error copying the image");
            return "settings";
        } catch (Exception e) {
            Files.deleteIfExists(Paths.get(pathImage, newFileName));
            e.printStackTrace();
            return VIEW_ERROR;
        }
    }
}
