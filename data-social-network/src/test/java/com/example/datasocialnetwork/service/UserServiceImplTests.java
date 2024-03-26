package com.example.datasocialnetwork.service;

import com.example.datasocialnetwork.common.Constants;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.config.JwtTokenUtil;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.impl.MailServiceImpl;
import com.example.datasocialnetwork.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.datasocialnetwork.common.Gender.getGenderById;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void testFindByEmail_UserNotFound(){
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        when(userRepository.findOneByEmail("test")).thenReturn(null);

        // Call the method
        UserInfoResponse responseEntity = userService.findByEmail("test");
        assertEquals(Constants.MESS_010, responseEntity.getError());
    }

    @Test
    public void testFindByEmail_AuthFail(){
        // Mock user details
        User userDetails = new User();
        userDetails.setUserName("abc");
        userDetails.setId(1L);
        userDetails.setEmail("test1@gmail.com");
        UserAuthDetails authUserDetails = new UserAuthDetails(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        User user = new User();
        user.setId(1L);
        user.setUserName("test");
        when(userRepository.findOneByEmail("test@gmail.com")).thenReturn(user);

        // Call the method
        assertThrows(UserNotFoundException.class, () -> {
            userService.findByEmail("test@gmail.com");
        });

    }

    @Test
    public void testFindByEmail_Success(){
        // Mock user details
        User userDetails = new User();
        userDetails.setUserName("abc");
        userDetails.setId(1L);
        userDetails.setEmail("test@gmail.com");
        UserAuthDetails authUserDetails = new UserAuthDetails(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        User user = new User();
        user.setId(1L);
        user.setUserName("abc");
        user.setEmail("test@gmail.com");
        user.setAddress("VN");
        user.setJob("dev");
        user.setPhone("123456789");
        user.setSex(1);
        user.setBirthday(LocalDate.now());
        user.setImage("image2.jpg");
        when(userRepository.findOneByEmail("test@gmail.com")).thenReturn(user);

        // Call the method
        // Call the method
        UserInfoResponse responseEntity = userService.findByEmail("test@gmail.com");

        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setId(user.getId().toString());
        userInfo.setEmail(user.getEmail());
        userInfo.setUserName(user.getUserName());
        userInfo.setBirthday(user.getBirthday() == null ? "": DateTimeFormatter.ISO_LOCAL_DATE.format(user.getBirthday()));
        userInfo.setAddress(user.getAddress());
        userInfo.setJob(user.getJob());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvata(user.getImage());
        userInfo.setSex(getGenderById(user.getSex()).name());
        assertEquals(userInfo, responseEntity);

    }

    @Test
    public void testFindById_UserNotFound_1(){
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        when(userRepository.findOneByUserName("abc")).thenReturn(null);

        // Call the method
        UserInfoResponse responseEntity = userService.findById(1L);
        assertEquals(Constants.MESS_010, responseEntity.getError());
    }

    @Test
    public void testFindById_UserNotFound_2(){
        // Mock user details
        User user = new User();
        user.setUserName("abc");
        user.setId(1L);
        UserAuthDetails authUserDetails = new UserAuthDetails(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        when(userRepository.findOneByUserName("abc")).thenReturn(user);
        when(userRepository.findOneById(1L)).thenReturn(null);

        // Call the method
        UserInfoResponse responseEntity = userService.findById(1L);
        assertEquals(Constants.MESS_013, responseEntity.getError());
    }

    @Test
    public void testFindById_Success(){
        // Mock user details
        User userDetails = new User();
        userDetails.setUserName("abc");
        userDetails.setId(1L);
        userDetails.setEmail("test@gmail.com");
        UserAuthDetails authUserDetails = new UserAuthDetails(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);

        // Mock repository methods
        User user = new User();
        user.setId(1L);
        user.setUserName("abc");
        user.setEmail("test@gmail.com");
        user.setAddress("VN");
        user.setJob("dev");
        user.setPhone("123456789");
        user.setSex(1);
        user.setBirthday(LocalDate.now());
        user.setImage("image2.jpg");
        when(userRepository.findOneByUserName("abc")).thenReturn(userDetails);
        when(userRepository.findOneById(1L)).thenReturn(user);

        // Call the method
        // Call the method
        UserInfoResponse responseEntity = userService.findById(1L);

        UserInfoResponse userInfo = new UserInfoResponse();
        userInfo.setId(user.getId().toString());
        userInfo.setUserName(user.getUserName());
        userInfo.setBirthday(user.getBirthday() == null ? "": DateTimeFormatter.ISO_LOCAL_DATE.format(user.getBirthday()));
        userInfo.setAddress(user.getAddress());
        userInfo.setJob(user.getJob());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvata(user.getImage());
        userInfo.setSex(getGenderById(user.getSex()).name());
        assertEquals(userInfo, responseEntity);

    }
//
//    @Test
//    public void testSearchUserByUserName_Success() {
//        User userDetails = new User();
//        userDetails.setUserName("abc");
//        userDetails.setId(1L);
//        userDetails.setEmail("test@gmail.com");
//        UserAuthDetails authUserDetails = new UserAuthDetails(userDetails);
//        SecurityContext securityContext = mock(SecurityContext.class);
//        SecurityContextHolder.setContext(securityContext);
//        Authentication authentication = mock(Authentication.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(authUserDetails);
//        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//
//        // Mock repository methods
//        User user = new User();
//        user.setId(1L);
//        user.setUserName("abc");
//        user.setEmail("test@gmail.com");
//        user.setAddress("VN");
//        user.setJob("dev");
//        user.setPhone("123456789");
//        user.setSex(1);
//        user.setBirthday(LocalDate.now());
//        user.setImage("image2.jpg");
//        when(userRepository.findOneByUserName("abc")).thenReturn(userDetails);
//
//        // Create a mock SearchUserRequestDTO
//        SearchUserRequestDTO searchUserRequestDTO = new SearchUserRequestDTO();
//        searchUserRequestDTO.setSearch("search query");
//        searchUserRequestDTO.setPage(1);
//        Pageable pageable = Pageable.unpaged();
//        List<User> users = new ArrayList<>();
//        users.add(user);
//        when(userRepository.findByUserNameContaining(eq("search query"), eq(pageable))).thenReturn(page);
//        when(page.getContent()).thenReturn(users);
//        when(page.getTotalPages()).thenReturn(1);
//
//        // Act
//        ResponseEntity<SearchResponse> responseEntity = userController.searchUsers(searchUserRequestDTO, pageable);
//
//        // Assert
//        SearchResponse searchResponse = responseEntity.getBody();
//        assert searchResponse != null;
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(Constants.CODE_OK, searchResponse.getCode());
//        assertEquals(2, searchResponse.getListUser().size());
//        assertEquals(1, searchResponse.getTotalPage());
//    }
//
//    private UserRepository mockUserRepository() {
//        UserRepository userRepository = mock(UserRepository.class);
//
//        List<User> userList = new ArrayList<>();
//        userList.add(new User());
//        userList.add(new User());
//
//        Page<User> page = new PageImpl<>(userList);
//
//        when(userRepository.findOneByUserName(anyString())).thenReturn(new User());
//        when(userRepository.findAll(any(Pageable.class))).thenReturn(page);
//        when(userRepository.findByUserNameContaining(anyString(), any(Pageable.class))).thenReturn(page);
//
//        return userRepository;
//    }

}
