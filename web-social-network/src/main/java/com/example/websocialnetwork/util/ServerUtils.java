package com.example.websocialnetwork.util;

import com.example.websocialnetwork.dto.reponse.UserInfo;

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
}
