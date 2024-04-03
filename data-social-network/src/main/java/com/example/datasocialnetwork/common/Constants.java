package com.example.datasocialnetwork.common;

public class Constants {
    public static final String LOGIN_USER_NOT_FOUND = "Tài khoản email không tồn tại.";
    public static final String LOGIN_PASSWORD_WRONG = "Password không đúng.";
    public static final String LOGIN_SUCCESS = "Login thành công. Vui lòng xác thực mã OTP gửi về mail.";
    public static final String SEND_EMAIL_FAILE = "ĐÃ say ra lỗi khi gửi email, vui lòng thực hiện lại.";
    public static final String OTP_IS_EXPIRED = "OTP đã hết hiệu lực, vui lòng thực hiện login lại..";
    public static final String OTP_NOT_CORRECT = "OTP không đúng, vui lòng nhập lại.";
    public static final String OTP_COMFIRM_SUCCESS = "OTP xác thực thành công.";
    public static final String EMAIL_IS_EXISTS = "Email đã tồn tại";
    public static final String REGISTER_USER_SUCCESS = "Đăng kí user mới thành công";
    public static final String FORGOT_PASSWORD_SUCCESS = "Đã đổi password thành công, mã password mới đã được gửi về email của bạn";
    public static final String TOKEN_IS_NULL = "Request không có token";
    public static final String TOKEN_INVALID = "Request có token không hợp lệ";
    public static final String USER_BY_TOKEN_NOT_FOUND = "Không có user phù hợp với token";
    public static final String EXPORT_FILE_ERR = "Đã xa ra lỗi khi tạo file báo cáo";
    public static final String EXPORT_FILE_SUCCESS = "TẠo file báo cáo thành công, link file: ";
    public static final String FILE_IS_NOT_FORMAT = "Ảnh phải thộc định dạng image/jpeg,image/png";
    public static final String UPDATE_AVATA_SUCCESS = "update avata thành công.";
    public static final String UPDATE_USER_FORBIDDEN = "Bạn không có quyền thực hiện update user này.";
    public static final String BRITHDAY_INVALID = "Brithday không hợp lệ";
    public static final String UPDATE_USER_ERR = "UserName không thể là null/ empty";
    public static final String UPDATE_USER_SUCCESS = "Update user thành công";
    public static final String UPDATE_PASSWORD_ERR = "password old không map";
    public static final String UPDATE_PASSWORD_SUCCESS = "update password thành công";
    public static final String USER_NOT_FOUND = "Không tìm thấy tài khoản user này.";
    public static final String CHECK_FRIENDSHIP_ERR1 = "Không thể check frend ship với chính bản thân mình.";
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
    public static final String MESS_013 = "User does not exist";
    public static final int LIMIT = 3;
    public static final int CODE_OK = 0;
    public static final int CODE_ERROR = 1;
    public static final String[] ENDPOINTS_PUBLIC = new String[] {
            "/",
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
            "/api/auth/**"
    };
    public static final String[] ENDPOINTS_PUBLIC_PATH = new String[] {
            "/swagger-ui.html", "/swagger-ui/", "/v3/api-docs",
            "/api/auth"
    };
}
