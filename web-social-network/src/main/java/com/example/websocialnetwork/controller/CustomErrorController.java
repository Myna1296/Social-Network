package com.example.websocialnetwork.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static com.example.websocialnetwork.common.Const.VIEW_ERR_2;

@Controller
@RequestMapping("/error")
public class CustomErrorController  implements ErrorController {

    @RequestMapping
    public String handleError(HttpServletRequest request) {
        // Lấy mã trạng thái HTTP từ request
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return VIEW_ERR_2; // Đặt tên trang lỗi 404 của bạn ở đây
            }
        }
        return VIEW_ERR_2; // Trang lỗi mặc định hoặc tùy chọn khác
    }

    @Override
    public String getErrorPath() {
        return VIEW_ERR_2;
    }
}
