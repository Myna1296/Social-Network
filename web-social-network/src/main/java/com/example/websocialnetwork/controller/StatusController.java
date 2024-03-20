package com.example.websocialnetwork.controller;

import com.example.websocialnetwork.dto.FriendRequestDTO;
import com.example.websocialnetwork.dto.StatusDTO;
import com.example.websocialnetwork.dto.reponse.CommentResponse;
import com.example.websocialnetwork.dto.reponse.FriendResponse;
import com.example.websocialnetwork.dto.reponse.StatusResponse;
import com.example.websocialnetwork.dto.reponse.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.websocialnetwork.common.Const.*;
import static com.example.websocialnetwork.common.Const.VIEW_ERR;
import static com.example.websocialnetwork.util.ServerUtils.getProfileImagesPath;
import static com.example.websocialnetwork.util.ServerUtils.getUserFromSession;
import static com.example.websocialnetwork.util.Validation.calculateTotalPages;

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
    public String showStatusPage(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        List<StatusResponse> statusList = new ArrayList<>();
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);
        statusList.add(status);
        statusList.add(status);
        model.addAttribute("statusList", statusList);
        return "status";
    }

    @GetMapping("/{id}")
    public String showStatusUserPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        List<StatusResponse> statusList = new ArrayList<>();
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);
        statusList.add(status);
        statusList.add(status);
        model.addAttribute("statusList", statusList);
        model.addAttribute("id", id);
        model.addAttribute("userName", "Test");
        return "status";
    }

    @GetMapping("info/{id}")
    public String showStatusInfoPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("user") == null) {
            return "redirect:/";
        }
        StatusResponse status = new StatusResponse();
        status.setTitle("Test");
        status.setUserImage("image1.jpg");
        status.setUserName("Test name");
        status.setLikeCount(7);
        status.setCommentCount(10);

        List<CommentResponse> listComment = new ArrayList<>();
        CommentResponse comment = new CommentResponse();
        comment.setUserImage("image2.jpg");
        comment.setUserName("Test");
        comment.setText("Text1111");
        comment.setCreateDate("2023-02-02");
        listComment.add(comment);

        model.addAttribute("status", status);
        model.addAttribute("listComment", listComment);
        model.addAttribute("id", id);
        model.addAttribute("userName", "Test");
        return "status-info";
    }

    @GetMapping("/add")
    public String addStatusPage(Model model, HttpServletRequest request) {
//        if (request.getSession().getAttribute("user") == null) {
//            return "redirect:/";
//        }
        StatusDTO statusDTO = new StatusDTO();
        model.addAttribute("status", statusDTO);
        model.addAttribute("isEdit", "false");;
        return "status-edit";
    }

    @PostMapping("/add")
    public String addNewStatus(@RequestParam("statusImage") MultipartFile imageFile,
                               @Valid @ModelAttribute("status") StatusDTO statusDTO,
                               BindingResult bindingResult, Model model,
                               HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            return "status-edit";
        }
        String contentType = imageFile.getContentType();
//        if(!allowedExtensions.contains(contentType)) {
//            model.addAttribute("error","File extension is not supported");
//            return "status-edit";
//        }
        UserInfo user = getUserFromSession(request);
        byte[] bytes = imageFile.getBytes();
        Path pathImages = getProfileImagesPath();

        // Xử lý logic khi không có lỗi

        return "redirect:/path/to/success/page";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        // Xử lý lưu trữ tệp ảnh vào thư mục và trả về đường dẫn
        // Ví dụ:
        // Path pathImages = getProfileImagesPath();
        // byte[] bytes = imageFile.getBytes();
        // Files.write(Paths.get(pathImages.toString(), imageFile.getOriginalFilename()), bytes);
        // return pathImages.toString() + "/" + imageFile.getOriginalFilename();
        return ""; // Trả về đường dẫn của tệp ảnh sau khi lưu trữ
    }
}
