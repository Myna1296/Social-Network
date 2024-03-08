package com.example.websocialnetwork.util;

import com.example.websocialnetwork.dto.reponse.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.websocialnetwork.common.Const.PROFILE_IMAGES;


public class ServerUtils {

    public static UserInfo getUserFromSession(HttpServletRequest request){
        return (UserInfo) request.getSession().getAttribute("user");
    }

    public static Path getProfileImagesPath() {
        return Paths.get(".").resolve(PROFILE_IMAGES);
    }

    public static String getNewFileName(String userName, MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String extension = getFileExtension(fileName);
        return userName + "." + extension;
    }

    private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index == -1 ? fileName : fileName.substring(index + 1);
    }
}
