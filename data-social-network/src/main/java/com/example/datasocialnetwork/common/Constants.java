package com.example.datasocialnetwork.common;

public class Constants {
    public static final String MESS_001 = "Username already exists";
    public static final String MESS_002 = "Email already exists";
    public static final String MESS_003 = "Account registration successful";
    public static final String MESS_004 = "Email login does not exist";
    public static final String MESS_005 = "Login password is incorrect";
    public static final String MESS_006 = "Sending code failed";
    public static final String MESS_007 = "User is not logged in, please log in first and then authenticate OTP";
    public static final String MESS_008 = "OTP is not correct, please re-enter.";
    public static final String MESS_009 = "OTP has expired, please login again";
    public static final String MESS_010 = "Account has been deleted";
    public static final String MESS_011 = "The file is not in the format image/jpeg,image/png";
    public static final String MESS_012 = "File extension is not supported";

    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = 1;
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/auth/**"
    };
}
