package com.example.datasocialnetwork.service.impl;

import com.example.datasocialnetwork.common.Gender;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.config.JwtTokenUtil;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;

import static com.example.datasocialnetwork.common.Gender.getGenderById;
import static com.example.datasocialnetwork.common.Gender.getGenderByName;
import static com.example.datasocialnetwork.common.Validation.checkBirthday;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User users= userRepository.findOneByEmail(email);
        if(users == null){
            userInfo.setError(Constants.MESS_010);
        } else {
            if (!userDetails.getUsername().equals(users.getUserName())){
                throw(new UserNotFoundException("Authentication information does not match"));
            }
            userInfo.setId(users.getId().toString());
            userInfo.setEmail(users.getEmail());
            userInfo.setUserName(users.getUserName());
            userInfo.setBirthday(users.getBirthday() == null ? "": DateTimeFormatter.ISO_LOCAL_DATE.format(users.getBirthday()));
            userInfo.setAddress(users.getAddress());
            userInfo.setJob(users.getJob());
            userInfo.setPhone(users.getPhone());
            userInfo.setAvata(users.getImage());
            userInfo.setSex(getGenderById(users.getSex()).name());
        }
        return userInfo;
    }

    @Override
    public ResponseEntity<?> updateImageUser(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByEmail(userInfo.getEmail());
        if (user == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!userDetails.getUsername().equals(user.getUserName())){
            throw(new UserNotFoundException("Authentication information does not match"));
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

    @Override
    @Transactional
    public ResponseEntity<?> updateProfileUser(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = findOneById(Long.parseLong(userInfo.getId()));
        if (user == null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, Constants.MESS_010);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if (!userDetails.getUsername().equals(user.getUserName())){
            throw(new UserNotFoundException("Authentication information does not match"));
        }
        String checkBirthday = checkBirthday(userInfo.getBirthday());
        if(checkBirthday != null){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "date không hợp ệ");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        if(userInfo.getJob() != null) {
            user.setJob(userInfo.getJob());
        }
        if(userInfo.getAddress() != null) {
            user.setAddress(userInfo.getAddress());
        }
        if(userInfo.getPhone() != null) {
            user.setPhone(userInfo.getPhone());
        }
        if(userInfo.getSex() !=null){
            user.setSex(getGenderByName(userInfo.getSex()));
        }
        if(userInfo.getBirthday() !=null & !userInfo.getBirthday().equals("") ){
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(userInfo.getBirthday(),dateFormatter);
            user.setBirthday(date);
        } else {
            user.setBirthday(null);
        }
        userRepository.save(user);
        ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(),userDetails.getPassword())){
            ResponseOk response = new ResponseOk(Constants.CODE_ERROR, "Passwords doesn't match");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getPassword()));
        userRepository.save(user);
        ResponseOk response = new ResponseOk(Constants.CODE_OK, "");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public User findOneById(Long id) {
        return userRepository.findOneById(id);
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
        user.setSex(getGenderByName(userDTO.getSex()));
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