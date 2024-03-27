package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.service.impl.MailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceTestImpl {

    @Mock
    private Random random;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private OtpRepository otpRepository;

    @InjectMocks
    private MailServiceImpl mailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mailService, "emailUsername", "test@example.com");
    }


    @Test
    public void sendCode_SendOK() {
        // Thiết lập một số giá trị mặc định và mock behavior
        when(otpRepository.findOneByEmail("test@example.com")).thenReturn(new Otp());
        when(random.nextInt(anyInt())).thenReturn(42);
        when(emailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        // Gọi phương thức cần kiểm tra
        boolean result = mailService.sendCode("test@example.com", "test", SendCodeType.LOGIN);

        // Kiểm tra kết quả
        assertTrue(result);
        verify(emailSender, times(1)).send(any(MimeMessage.class)); // Kiểm tra phương thức send của emailSender được gọi một lần
        verify(otpRepository, times(1)).save(any(Otp.class)); // Kiểm tra phương thức save của otpRepository được gọi một lần
    }

    @Test
    public void sendCode_SendFailed() {
        // Thiết lập một số giá trị mặc định và mock behavior
        when(otpRepository.findOneByEmail("test@example.com")).thenReturn(null); // Không tìm thấy Otp
        when(random.nextInt(anyInt())).thenReturn(42); // Giả lập phương thức renderRandom()
        when(emailSender.createMimeMessage()).thenReturn(null); // Giả lập việc tạo MimeMessage không thành công

        // Gọi phương thức cần kiểm tra
        boolean result = mailService.sendCode("test@example.com", "test", SendCodeType.LOGIN);

        // Kiểm tra kết quả
        assertFalse(result);
        verify(emailSender, never()).send(any(MimeMessage.class)); // Kiểm tra phương thức send của emailSender không được gọi
        verify(otpRepository, never()).save(any(Otp.class)); // Kiểm tra phương thức save của otpRepository không được gọi
    }

    @Test
    public void sendCode_OTPNull(){
        when(otpRepository.findOneByEmail("test")).thenReturn(null);
        when(random.nextInt(anyInt())).thenReturn(42);
        assertFalse(mailService.sendCode("test", "test", SendCodeType.LOGIN));
    }

    @Test
    public void sendNewPassword_SendOK() {

        // Gọi phương thức cần kiểm tra
        when(emailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        boolean result = mailService.sendNewPassword("test@example.com", "test","", SendCodeType.LOGIN);

        // Kiểm tra kết quả
        assertTrue(result);
    }

    @Test
    public void sendNewPassword_SendFaile() {

        // Gọi phương thức cần kiểm tra
        boolean result = mailService.sendNewPassword("test@example.com", "test","", SendCodeType.RECOVERY);

        // Kiểm tra kết quả
        assertFalse(result);
    }
}
