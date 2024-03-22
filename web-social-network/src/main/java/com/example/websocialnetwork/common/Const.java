package com.example.websocialnetwork.common;

public class Const {
    public static final String VIEW_LOGIN ="index";
    public static final String VIEW_COMFIRM_REGISTER ="E002";
    public static final String VIEW_ERROR ="E003";
    public static final String VIEW_COMFIRM_OTP ="E004";
    public static final String VIEW_ERR = "E005";
    public static final String VIEW_ERR_2 = "E006";

    public static final String API_REGISTER ="/api/auth/register";
    public static final String API_LOGIN ="/api/auth/login";
    public static final String API_COMFIRM_OTP_LOGIN ="/api/auth/comfirm-otp-login";
    public static final String API_FORGOT_PASSWORD ="/api/auth/forgot-password";
    public static final String API_USER_INFO = "/user/user-info/{email}";
    public static final String API_USER_INFO_BY_ID = "/user/profile-user/{id}";
    public static final String API_UPDATE_IMAGE = "/user/update-image";
    public static final String API_UPDATE_PROFILE = "/user/update-profile-user";
    public static final String API_UPDATE_PASSWORD = "/user/update-password";
    public static final String API_GET_FRIENF_OF_USER = "/friends/get-all";
    public static final String API_GET_REQUEST_USER_NOT_ACCEPTE = "/friends/get-request-user-notaccepte";
    public static final String API_GET_REQUEST_NOT_ACCEPTE_TO_USER = "/friends/get-request-notaccepte-touser";
    public static final String API_CHECK_FRIEND_SHIP = "/friends/check-friend-ship/{id}";
    public static final String API_SEARCH_USER = "/user/search-user";
    public static final String API_ADD_FRIEND = "/friends/add-to-friends/{id}";
    public static final String API_DELETE_FRIENDSHIP = "/friends/delete";
    public static final String API_ACCEPTE_FRIENDSHIP = "/friends/accepte";
    public static final String API_GET_ALL_STATUS_USER = "/status/all";
    public static final String API_GET_ALL_STATUS_FRIEND = "/status/all/friend";
    public static final String API_DELETE_STATUS = "/status/delete/{postId}";
    public static final String API_ADD_STATUS = "/status/add";
    public static final String API_SEARCH_STATUS = "/status/search/{id}";
    public static final String API_UPDATE_STATUS = "/status/update";
    public static final String API_SEARCH_COMMENT = "/comment/search";
    public static final String API_ADD_COMMENT = "/comment/add";
    public static final String API_DELETE_COMMENT = "/comment/delete/{id}";
    public static final String API_EXPORT_FILE = "/api/export/export-file";
    public static final String API_ADD_LIKE = "/like/add/{id}";
    public static final String API_DELETE_LIKE = "/like/delete/{id}";

    public static final String PROFILE_IMAGES = "profileImages";
    public static final String MESS_001 = "This account does not exist";
    public static final String MESS_002 = "Page does not exist or has no data.";
    public static final  int LIMIT  = 3;

}
