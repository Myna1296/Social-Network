package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Gender;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.config.JwtTokenUtil;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.LoginDTO;
import com.example.datasocialnetwork.dto.request.OTPComfirmDTO;
import com.example.datasocialnetwork.dto.request.UserDTO;
import com.example.datasocialnetwork.dto.request.UserInfo;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.datasocialnetwork.common.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl() {
        this.passwordEncoder = new StandardPasswordEncoder();
    }

    /*
    Handles user registration
    Create by NgaPLT 2024/03/06
    <param>UserDTO</param>
    <returns></returns>
     */
    @Override
    @Transactional
    public ResponseEntity<?> createUser(UserDTO userDto) {
        //Check if user name and email exist or not
        User user = convertUserDtoToUser(userDto);
        String result = validateInfo(user);
        if(result != null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR,result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        userRepository.save(user);
        ResponseOk response = new ResponseOk(Constants.CODE_OK,Constants.MESS_003);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public ResponseEntity<?> loginUser(LoginDTO userLogin) {
        User user = userRepository.findOneByEmail(userLogin.getEmail());
        if (user == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_004);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if ( !passwordEncoder.matches(userLogin.getPassword(),user.getPassword())){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_005);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        boolean sednMail = mailService.sendCode(user.getEmail(), user.getUserName(), SendCodeType.LOGIN);
        if( sednMail){
            ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_006);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> comfirmOTPLogin(OTPComfirmDTO otpComfirm) {
        Otp otp = otpRepository.findOneByEmail(otpComfirm.getEmail());
        if(otp == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_007);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(otp.getCode().equals(otpComfirm.getOtp())){
            LocalDateTime otpDate = LocalDateTime.now().minusMinutes(5);
            if( otpDate.compareTo(otp.getCreatedDate()) > 0 ) {
                ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_009);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            User user = userRepository.findOneByEmail(otp.getEmail());
            String token = jwtTokenUtil.generateToken(user.getUserName());
            user.setToken(token);
            userRepository.save(user);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
            return new ResponseEntity<>(response,headers, HttpStatus.OK);
        }
        ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_008);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public UserInfoResponse findByEmail(String email) {
        UserInfoResponse userInfo = new UserInfoResponse();
        User users= userRepository.findOneByEmail(email);
        if(users == null){
            userInfo.setError(Constants.MESS_010);
        } else {
            userInfo.setId(users.getId().toString());
            userInfo.setEmail(users.getEmail());
            userInfo.setUserName(users.getUserName());
            userInfo.setBirthday(users.getBirthday());
            userInfo.setAddress(users.getAddress());
            userInfo.setJob(users.getJob());
            userInfo.setPhone(users.getPhone());
            userInfo.setAvata(users.getImage());
            userInfo.setSex(Gender.getGenderById(users.getSex()).name());
        }
        return userInfo;
    }

    @Override
    public ResponseEntity<?> updateImageUser(UserInfo userInfo) {
        User user = userRepository.findOneByEmail(userInfo.getEmail());
        if (user == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        try {
            if (!isImageFile(userInfo.getAvata())){
                ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_011);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }catch (IOException ex){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_012);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        user.setImage(userInfo.getAvata());
        userRepository.save(user);
        ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public UserDetails findUserByToken(String token){
        User user = userRepository.findOneByToken(token);
        return  new UserAuthDetails(user);
    }

    private String validateInfo(User user){
        if(existsByUsername(user.getUserName())) {
            return Constants.MESS_001;
        }
        if(existsByEmail(user.getEmail())){
            return Constants.MESS_002;
        }
        return null;
    }

    private User convertUserDtoToUser(UserDTO userDTO){
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setUserName(userDTO.getUserName());
        user.setAddress(userDTO.getAddress());
        user.setPhone(userDTO.getPhone());
        user.setSex(Gender.getGenderByName(userDTO.getSex()));
        user.setImage(userDTO.getImage());
        user.setBirthday(userDTO.getBirthday());
        user.setJob(userDTO.getJob());
        if(userDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return user;
    }
    private static boolean isImageFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        // Check if the file is of type image/jpeg or image/png
        String contentType = Files.probeContentType(path);
        return contentType != null &&
                (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
}
