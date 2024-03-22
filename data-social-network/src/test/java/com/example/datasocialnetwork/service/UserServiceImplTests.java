package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.config.JwtTokenUtil;
import com.example.datasocialnetwork.dto.request.LoginDTO;
import com.example.datasocialnetwork.dto.request.OTPComfirmDTO;
import com.example.datasocialnetwork.dto.request.UserDTO;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.impl.MailServiceImpl;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpRepository otpRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private MailServiceImpl mailService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        this.passwordEncoder = new StandardPasswordEncoder();
    }

    @Test
    public void testLoginUser_Success() {
        String email = "test@example.com";
        String password = "password";

        LoginDTO userLogin = new LoginDTO();
        userLogin.setEmail(email);
        userLogin.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        when(userRepository.findOneByEmail(email)).thenReturn(user);
        when(mailService.sendCode(email, user.getUserName(), SendCodeType.LOGIN)).thenReturn(true);

        ResponseEntity<?> responseEntity = userService.loginUser(userLogin);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Check data  ResponseEntity
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(Constants.CODE_OK, response.getCode());
        assertEquals("", response.getMessage());
    }

    @Test
    public void testLoginUser_UserNotFound() {
        String email = "test@example.com";
        String password = "password";

        LoginDTO userLogin = new LoginDTO();
        userLogin.setEmail(email);
        userLogin.setPassword(password);

        when(userRepository.findOneByEmail(email)).thenReturn(null);

        ResponseEntity<?> responseEntity = userService.loginUser(userLogin);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_004, response.getMessage());
    }

    @Test
    public void testLoginUser_IncorrectPassword() {
        String email = "test@example.com";
        String password = "password1";

        LoginDTO userLogin = new LoginDTO();
        userLogin.setEmail(email);
        userLogin.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("password2"));

        when(userRepository.findOneByEmail(email)).thenReturn(user);

        ResponseEntity<?> responseEntity = userService.loginUser(userLogin);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_005, response.getMessage());
    }

    @Test
    public void testLoginUser_SendMailFailed() {
        String email = "test@example.com";
        String password = "password";

        LoginDTO userLogin = new LoginDTO();
        userLogin.setEmail(email);
        userLogin.setPassword(password);

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        when(userRepository.findOneByEmail(email)).thenReturn(user);
        when(mailService.sendCode(email, user.getUserName(), SendCodeType.LOGIN)).thenReturn(false);

        ResponseEntity<?> responseEntity = userService.loginUser(userLogin);
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_006, response.getMessage());
    }

    @Test
    public void testComfirmOTPLogin_Success() {
        // Mocking data
        String email = "test@example.com";
        String otpCode = "123456";

        OTPComfirmDTO otpComfirm = new OTPComfirmDTO();
        otpComfirm.setEmail(email);
        otpComfirm.setOtp(otpCode);

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(otpCode);
        otp.setCreatedDate(LocalDateTime.now());

        User user = new User();
        user.setEmail(email);
        user.setUserName("TestUser");

        // Mocking repository responses
        when(otpRepository.findOneByEmail(email)).thenReturn(otp);
        when(jwtTokenUtil.generateToken(user.getUserName())).thenReturn("testToken");
        when(userRepository.findOneByEmail(email)).thenReturn(user);
        // Mocking the save method of userRepository
        when(userRepository.save(any(User.class))).thenReturn(new User());
        // Call the method under test
        ResponseEntity<?> responseEntity = userService.comfirmOTPLogin(otpComfirm);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(Constants.CODE_OK, response.getCode());
        assertTrue(response.getMessage().isEmpty());

    }

    @Test
    public void testComfirmOTPLogin_OTPNotFound() {
        // Mocking data
        String email = "test@example.com";
        String otpCode = "123456";
        String invalidOtp = "654321";

        OTPComfirmDTO otpComfirm = new OTPComfirmDTO();
        otpComfirm.setEmail(email);
        otpComfirm.setOtp(invalidOtp);

        // Mocking repository responses
        when(otpRepository.findOneByEmail(email)).thenReturn(null);

        // Call the method under test
        ResponseEntity<?> responseEntity = userService.comfirmOTPLogin(otpComfirm);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_007, response.getMessage());
    }

    @Test
    public void testComfirmOTPLogin_InvalidOTP() {
        // Mocking data
        String email = "test@example.com";
        String otpCode = "123456";
        String invalidOtp = "654321";

        OTPComfirmDTO otpComfirm = new OTPComfirmDTO();
        otpComfirm.setEmail(email);
        otpComfirm.setOtp(invalidOtp);

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(otpCode);
        otp.setCreatedDate(LocalDateTime.now());

        // Mocking repository responses
        when(otpRepository.findOneByEmail(email)).thenReturn(otp);

        // Call the method under test
        ResponseEntity<?> responseEntity = userService.comfirmOTPLogin(otpComfirm);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_008, response.getMessage());
    }

    @Test
    public void testComfirmOTPLogin_OTPExpired() {
        // Mocking data
        String email = "test@example.com";
        String otpCode = "123456";
        OTPComfirmDTO otpComfirm = new OTPComfirmDTO();
        otpComfirm.setEmail(email);
        otpComfirm.setOtp(otpCode);

        Otp otp = new Otp();
        otp.setEmail(email);
        otp.setCode(otpCode);
        otp.setCreatedDate(LocalDateTime.now().minusDays(1));

        // Mocking repository responses
        when(otpRepository.findOneByEmail(email)).thenReturn(otp);

        // Call the method under test
        ResponseEntity<?> responseEntity = userService.comfirmOTPLogin(otpComfirm);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ResponseOk response = (ResponseOk) responseEntity.getBody();
        assertNotNull(response);
        assertEquals(Constants.CODE_ERROR, response.getCode());
        assertEquals(Constants.MESS_009, response.getMessage());
    }

    @Test
    public void testCreateUser_Success() {
        // Tạo một đối tượng UserDTO hợp lệ
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        userDTO.setUserName("testUser");

        // Giả mạo phương thức existsByUsername và existsByEmail trả về false (không tồn tại)
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        // Khi gọi phương thức createUser
        ResponseEntity<?> responseEntity = userService.createUser(userDTO);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals(Constants.MESS_003, ((ResponseOk) responseEntity.getBody()).getMessage());
    }

    @Test
    public void testCreateUser_ExistUserName() {
        // Tạo một đối tượng UserDTO hợp lệ
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        userDTO.setUserName("testUser");

        // Giả mạo phương thức existsByUsername và existsByEmail trả về false (không tồn tại)
        when(userRepository.existsByUserName(anyString())).thenReturn(true);

        // Khi gọi phương thức createUser
        ResponseEntity<?> responseEntity = userService.createUser(userDTO);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals(Constants.MESS_001, ((ResponseOk) responseEntity.getBody()).getMessage());
    }

    @Test
    public void testCreateUser_ExistEmail() {
        // Tạo một đối tượng UserDTO hợp lệ
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
        userDTO.setUserName("testUser");

        // Giả mạo phương thức existsByUsername và existsByEmail trả về false (không tồn tại)
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Khi gọi phương thức createUser
        ResponseEntity<?> responseEntity = userService.createUser(userDTO);

        // Kiểm tra kết quả trả về
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals(Constants.MESS_002, ((ResponseOk) responseEntity.getBody()).getMessage());
    }

    @Test
    public void testForgotPassword_UserNotFound() {
        // Mocking userRepository to return null when findOneByEmail is called
        when(userRepository.findOneByEmail(anyString())).thenReturn(null);

        // Call the forgotPassword method with a non-existent email
        ResponseEntity<?> responseEntity = userService.forgotPassword("nonexistent@example.com");

        // Verify that the correct error response is returned
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals(Constants.MESS_004, ((ResponseOk) responseEntity.getBody()).getMessage());

        // Ensure that userRepository.findOneByEmail() is called once
        verify(userRepository, times(1)).findOneByEmail(anyString());
    }

    @Test
    public void testForgotPassword_EmailSendingFailure() {
        // Mocking userRepository to return a user
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findOneByEmail(anyString())).thenReturn(user);

        // Mocking mailService to return false when sendNewPassword is called
        when(mailService.sendNewPassword(any(), any(), any(), any())).thenReturn(false);

        // Call the forgotPassword method
        ResponseEntity<?> responseEntity = userService.forgotPassword("test@example.com");

        // Verify that the correct error response is returned
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_ERROR, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals("An error occurred while sending email", ((ResponseOk) responseEntity.getBody()).getMessage());

        // Ensure that userRepository.findOneByEmail() is called once
        verify(userRepository, times(1)).findOneByEmail(anyString());
        // Ensure that mailService.sendNewPassword() is called once
        verify(mailService, times(1)).sendNewPassword(any(), any(), any(), any());
    }

    @Test
    public void testForgotPassword_Success() {
        // Mocking userRepository to return a user
        User user = new User();
        user.setEmail("test@example.com");
        user.setUserName("testUser");
        when(userRepository.findOneByEmail(anyString())).thenReturn(user);

        // Mocking mailService to return true when sendNewPassword is called
        when(mailService.sendNewPassword(anyString(), anyString(), anyString(), any(SendCodeType.class))).thenReturn(true);

        // Call the forgotPassword method
        ResponseEntity<?> responseEntity = userService.forgotPassword("test@example.com");

        // Verify that the correct success response is returned
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Constants.CODE_OK, ((ResponseOk) responseEntity.getBody()).getCode());
        assertEquals("An email has been sent to reset your password.", ((ResponseOk) responseEntity.getBody()).getMessage());

        // Ensure that userRepository.findOneByEmail() is called once
        verify(userRepository, times(1)).findOneByEmail(anyString());
        // Ensure that mailService.sendNewPassword() is called once
        verify(mailService, times(1)).sendNewPassword(anyString(), anyString(), anyString(), any(SendCodeType.class));
        // Ensure that userRepository.save() is called once
        verify(userRepository, times(1)).save(any(User.class));
    }

}
