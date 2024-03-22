package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.SendCodeType;

public interface MailService {
    boolean sendCode(String email, String username, SendCodeType sendCodeType);
    boolean sendNewPassword (String email, String username,String code, SendCodeType sendCodeType);
}
