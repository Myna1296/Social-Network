package com.example.websocialnetwork.exceptionHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleUserNotFoundException(HttpServletRequest request, RuntimeException e) {
        log.error(">>>>>>>>>>  Not Found Exception");
        log.error(e.getMessage(), e);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setViewName("E005");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(HttpServletRequest request, Exception e) {
        log.error(">>>>>>>>>> Exception");
        log.error(e.getMessage(), e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", e.getMessage());
        modelAndView.setViewName("E006");
        return modelAndView;
    }

}
