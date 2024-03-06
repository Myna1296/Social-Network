package com.example.websocialnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import static com.example.websocialnetwork.common.Const.VIEW_LOGIN;

@Controller
public class ErrorController {
    @ExceptionHandler(RuntimeException.class)
    @GetMapping("/error")
    public String handle404Error() {
        return VIEW_LOGIN;
    }
}
