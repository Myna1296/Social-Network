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
    public static final String API_FORGOT_PASSWORD ="/api/auth/forgot-password/{email}";
    public static final String API_USER_INFO = "/api/user/profile";
    public static final String API_USER_INFO_BY_ID = "/api/user/profile/{userId}";
    public static final String API_UPDATE_IMAGE = "/api/user/update-avata";
    public static final String API_UPDATE_PROFILE = "/api/user/update-profile";
    public static final String API_UPDATE_PASSWORD = "/api/user/update-password";
    public static final String API_SEARCH_USER = "/api/user/search";
    public static final String API_GET_FRIENF_OF_USER = "/api/friends/accepte-friend-request?pageIndex={index}&pageSize={size}";
    public static final String API_USER_SENT_FRIEND_REQUEST = "/api/friends/user-sent-friend-request?pageIndex={index}&pageSize={size}";
    public static final String API_WAITING_USER_TO_ACCEPT = "/api/friends/waiting-user-to-accept?pageIndex={index}&pageSize={size}";
    public static final String API_CHECK_FRIEND_SHIP = "/api/friends/check-friend-ship/{userId}";
    public static final String API_ADD_FRIEND = "/api/friends/sent-friend-request/{id}";
    public static final String API_DELETE_FRIENDSHIP = "/api/friends/delete";
    public static final String API_ACCEPTE_FRIENDSHIP = "/api/friends/accepte/{id}";
    public static final String API_GET_ALL_STATUS_USER = "/api/status/get-of-user";
    public static final String API_NEWS_FEED = "/api/status/newsfeed?pageIndex={index}&pageSize={size}";
    public static final String API_DELETE_STATUS = "/api/status/delete/{id}";
    public static final String API_ADD_STATUS = "/api/status/add";
    public static final String API_SEARCH_STATUS = "/api/status/info/{id}";
    public static final String API_UPDATE_STATUS = "/api/status/update";
    public static final String API_SEARCH_COMMENT = "/api/comment";
    public static final String API_ADD_COMMENT = "/api/comment/add";
    public static final String API_DELETE_COMMENT = "/api/comment/delete/{id}";
    public static final String API_EXPORT_FILE = "/api/export-file";
    public static final String API_ADD_LIKE = "/api/like/add/{id}";
    public static final String API_DELETE_LIKE = "/api/like/delete/{id}";

    public static final String PROFILE_IMAGES = "profileImages";
    public static final String MESS_001 = "This account does not exist";
    public static final String MESS_002 = "Page does not exist or has no data.";
    public static final  int PAGE_SIZE  = 5;

}
