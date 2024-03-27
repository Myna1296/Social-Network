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
    public static final String API_USER_INFO = "/api/user/user-info/{email}";
    public static final String API_USER_INFO_BY_ID = "/api/user/profile-user/{id}";
    public static final String API_UPDATE_IMAGE = "/api/user/update-image";
    public static final String API_UPDATE_PROFILE = "/api/user/update-profile-user";
    public static final String API_UPDATE_PASSWORD = "/api/user/update-password";
    public static final String API_GET_FRIENF_OF_USER = "/api/friends/get-friend/{pageId}";
    public static final String API_GET_REQUEST_USER_NOT_ACCEPTE = "/api/friends/get-request-user-notaccepte/{pageId}";
    public static final String API_GET_REQUEST_NOT_ACCEPTE_TO_USER = "/api/friends/get-request-notaccepte-touser/{pageId}";
    public static final String API_CHECK_FRIEND_SHIP = "/api/friends/check-friend-ship/{id}";
    public static final String API_SEARCH_USER = "/api/api/user/search-user";
    public static final String API_ADD_FRIEND = "/api/friends/add-to-friends/{id}";
    public static final String API_DELETE_FRIENDSHIP = "/api/friends/delete";
    public static final String API_ACCEPTE_FRIENDSHIP = "/api/friends/accepte";
    public static final String API_GET_ALL_STATUS_USER = "/api/status/all/{pageId}";
    public static final String API_GET_ALL_STATUS_FRIEND = "/api/status/all/friend/{pageId}";
    public static final String API_DELETE_STATUS = "/api/status/delete/{postId}";
    public static final String API_ADD_STATUS = "/api/status/add";
    public static final String API_SEARCH_STATUS = "/api/status/search/{id}";
    public static final String API_UPDATE_STATUS = "/api/status/update";
    public static final String API_SEARCH_COMMENT = "/api/comment/search";
    public static final String API_ADD_COMMENT = "/api/comment/add";
    public static final String API_DELETE_COMMENT = "/api/comment/delete/{id}";
    public static final String API_EXPORT_FILE = "/api/export/export-file";
    public static final String API_ADD_LIKE = "/api/like/add/{id}";
    public static final String API_DELETE_LIKE = "/api/like/delete/{id}";

    public static final String PROFILE_IMAGES = "profileImages";
    public static final String MESS_001 = "This account does not exist";
    public static final String MESS_002 = "Page does not exist or has no data.";
    public static final  int LIMIT  = 3;

}
