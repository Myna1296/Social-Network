package com.example.websocialnetwork.common;

public class Const {
    public static final String VIEW_LOGIN ="index";
    public static final String VIEW_COMFIRM_REGISTER ="E002";
    public static final String VIEW_ERROR ="E003";
    public static final String VIEW_COMFIRM_OTP ="E004";
    public static final String VIEW_ERR = "E005";
    public static final String VIEW_ERR_2 = "E006";

    public static final String API_REGISTER ="/auth/register";
    public static final String API_LOGIN ="/auth/login";
    public static final String API_COMFIRM_OTP_LOGIN ="/auth/comfirm-otp-login";
    public static final String API_USER_INFO = "/user/user-info/{email}";
    public static final String API_USER_INFO_BY_ID = "/user/profile-user/{id}";
    public static final String API_UPDATE_IMAGE = "/user/update-image";
    public static final String API_UPDATE_PROFILE = "/user/update-profile-user";
    public static final String API_UPDATE_PASSWORD = "/user/update-password";
    public static final String API_GET_FRIENF_OF_USER = "/friends/get-all";
    public static final String API_CHECK_FRIEND_SHIP = "/friends/check-friend-ship/{id}";

    public static final String PROFILE_IMAGES = "profileImages";
    public static final String MESS_001 = "This account does not exist";
    public static final String MESS_002 = "Page does not exist or has no data.";
    public static final  int LIMIT  = 3;

}
