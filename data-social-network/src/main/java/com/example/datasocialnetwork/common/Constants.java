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
    public static final String ADD_NEW_STATUS_SUCCESS = "tạo status mới thành công";
    public static final String STATUS_NOT_FOUND = "Status không tồn tại";
    public static final String STATUS_UPDATE_FORBIDDEN = "Bạn không có quyền thực hiện update status này.";
    public static final String STATUS_DELETE_FORBIDDEN = "Bạn không có quyền thực hiện xóa status này.";
    public static final String STATUS_UPDATE_SUCCESS = "Update status thành công.";
    public static final String STATUS_DELETE_SUCCESS = "DELETE status thành công.";
    public static final String LIKE_ERR = "Bạn đã like , không thể thực hiện like tiếp nữa.";
    public static final String UNLIKE_ERR = "Bạn chưa like , không thể thực hiện unlike tiếp nữa.";
    public static final String LIKE_SUCCESS = "Bạn đã like thành công.";
    public static final String UNLIKE_SUCCESS = "Bạn đã unlike thành công.";
    public static final String COMMENT_NOT_FOUND = "Không tồn tại comment muốn xóa";
    public static final String COMMENT_FORBIDDEN = "Bạn không có quyền xóa comment này";
    public static final String COMMENT_DELETE_SUCCESS = "Xóa comment thành công";
    public static final String COMMENT_ADD_SUCCESS = "Thêm  comment thành công";
    public static final String FRIEND_ADD_ERR1 = "Không thể gửi yêu cầu kết bạn cho chính mình";
    public static final String FRIEND_ADD_ERR2 = "Đã là bạn bè";
    public static final String FRIEND_ADD_ERR3 = "Lời mời kết bạn đã tồn tại.";
    public static final String FRIEND_ADD_SUCCESS = "Gửi lời mời kết bạn thành công";
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
