package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MailServiceImpl implements MailService {

    @Value("${email.username}")
    private String emailUsername;
    private  JavaMailSender emailSender ;
    private  int MIN_CODE = 10106383, MAX_CODE = 99898981;
    private  int MINUTE_LIMIT = 5;
    private Random random ;

    @Autowired
    public MailServiceImpl(Random random, JavaMailSender javaMailSender) {
        this.random = random;
        this.emailSender = javaMailSender;
    }
    @Autowired
    private OtpRepository otpRepository;
    @Override
    public boolean sendCode(String email, String username, SendCodeType sendCodeType) {
        String title = "Confirm %s";
        String content = "<h1>Hello "+username+"!</h1>\n" +
                "<p>Your security code is: <i><b><u>%s</u></b></i></p>"+
                "<p>Please do not give this code to others.</p>" +
                "<p>This is a one-time use code and can be used for a maximum of 5 minutes.</p>"+
                "<p>Sincerely thank you!</p>";
        String code  = String.valueOf(renderRandom());
        String type;
        if (sendCodeType == SendCodeType.RECOVERY) {
            type = SendCodeType.RECOVERY.getName();
        } else {
            type = SendCodeType.LOGIN.getName();
        }
        title= String.format(title,type);
        content = String.format(content,code,type);

        Otp otp = otpRepository.findOneByEmail(email);
        Otp newOtp = new Otp();
        newOtp.setCode(code);
        newOtp.setEmail(email);
        newOtp.setCreatedDate(LocalDateTime.now());
        if(otp != null){
            deleteByEmail(otp);
        }
        if( sendSimpleMessage(email, title, content)){
            otpRepository.save(newOtp);
            return true;
        }
        return false;
    }
    public void deleteByEmail(Otp otp) {
        otpRepository.delete(otp);
    }
    private int renderRandom(){
        return random.nextInt((MAX_CODE - MIN_CODE) + 1) + MIN_CODE;
    }
    public boolean sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            message.setSubject(subject,"UTF-8");
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true,"UTF-8");
            helper.setFrom(emailUsername);
            helper.setTo(to);
            helper.setText(text, true);
            emailSender.send(message);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }
}
