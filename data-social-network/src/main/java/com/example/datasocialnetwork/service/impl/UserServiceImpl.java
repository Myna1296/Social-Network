package com.example.datasocialnetwork.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.datasocialnetwork.common.SendCodeType;
import com.example.datasocialnetwork.config.JwtTokenUtil;
import com.example.datasocialnetwork.config.UserAuthDetails;
import com.example.datasocialnetwork.dto.request.*;
import com.example.datasocialnetwork.dto.response.SearchResponse;
import com.example.datasocialnetwork.dto.response.UserInfoResponse;
import com.example.datasocialnetwork.entity.Otp;
import com.example.datasocialnetwork.entity.User;
import com.example.datasocialnetwork.dto.response.ResponseOk;
import com.example.datasocialnetwork.exceptions.UserNotFoundException;
import com.example.datasocialnetwork.repository.OtpRepository;
import com.example.datasocialnetwork.repository.UserRepository;
import com.example.datasocialnetwork.service.MailService;
import com.example.datasocialnetwork.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.datasocialnetwork.common.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private  Cloudinary cloudinary;

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
    public ResponseEntity<?> createUser(RegisterUserRequest userDto) {
        //Check if user name and email exist or not
        User user = convertUserDtoToUser(userDto);
        String result = validateInfo(user);
        if(result != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.REGISTER_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<?> getProfileUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        UserInfo userInfo = convertUserToUserInfo(user);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
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
    public ResponseEntity<?> loginUser(LoginRequest userLogin) {
        User user = userRepository.findOneByEmail(userLogin.getEmail());
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        if ( !passwordEncoder.matches(userLogin.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.LOGIN_PASSWORD_WRONG);
        }
        boolean sednMail = mailService.sendCode(user.getEmail(), user.getUserName(), SendCodeType.LOGIN);
        if( sednMail){
            return ResponseEntity.status(HttpStatus.OK).body(Constants.LOGIN_SUCCESS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.SEND_EMAIL_FAILE);
    }

    @Override
    public ResponseEntity<?> comfirmOTPLogin(ComfirmOTPRequest otpComfirm) {
        Otp otp = otpRepository.findOneByEmail(otpComfirm.getEmail());
        if(otp == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        if(otp.getCode().equals(otpComfirm.getOtp())){
            LocalDateTime otpDate = LocalDateTime.now().minusMinutes(5);
            if( otpDate.compareTo(otp.getCreatedDate()) > 0 ) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.OTP_IS_EXPIRED);
            }
            User user = userRepository.findOneByEmail(otp.getEmail());
            String token = jwtTokenUtil.generateToken(user.getId().toString());
            user.setToken(token);
            userRepository.save(user);
            UserInfo userInfo = convertUserToUserInfo(user);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return  new ResponseEntity<>(userInfo, headers, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.OTP_NOT_CORRECT);
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
    public UserInfoResponse findById(Long id) {
        UserInfoResponse userInfo = new UserInfoResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User users= userRepository.findOneByUserName(userDetails.getUsername());
        if(users == null){
            userInfo.setError(Constants.MESS_010);
        } else {
            User usersById = userRepository.findOneById(id);
            if(usersById == null){
                userInfo.setError(Constants.MESS_013);
            } else {
                userInfo.setId(usersById.getId().toString());
                userInfo.setUserName(usersById.getUserName());
                userInfo.setBirthday(usersById.getBirthday() == null ? "" : DateTimeFormatter.ISO_LOCAL_DATE.format(usersById.getBirthday()));
                userInfo.setAddress(usersById.getAddress());
                userInfo.setJob(usersById.getJob());
                userInfo.setPhone(usersById.getPhone());
                userInfo.setAvata(usersById.getImage());
                userInfo.setSex(getGenderById(usersById.getSex()).name());
            }
        }
        return userInfo;
    }

    @Override
    public ResponseEntity<?> updateImageUser(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneById(Long.parseLong(userDetails.getUserID()));
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        try {
            if (!isImageFile(file.getContentType())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.FILE_IS_NOT_FORMAT);
            }
            Map data = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type","auto"));
            String url = (String) data.get("secure_url");
            user.setImage(url);
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.UPDATE_AVATA_SUCCESS);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateProfileUser(UserInfo userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = findOneById(Long.parseLong(userDetails.getUserID()));
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        if (Long.parseLong(userDetails.getUserID()) != user.getId()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Constants.UPDATE_USER_FORBIDDEN);
        }
        String checkBirthday = checkBirthday(userInfo.getBirthday());
        if(checkBirthday != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.BRITHDAY_INVALID);
        }
        if (userInfo.getUserName() == null || userInfo.getUserName().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.UPDATE_USER_ERR);
        } else {
            user.setUserName(userInfo.getUserName());
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
        if(userInfo.getBirthday() ==null || userInfo.getBirthday().equals("") ){
            user.setBirthday(null);
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(userInfo.getBirthday(),dateFormatter);
            user.setBirthday(date);
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.UPDATE_USER_SUCCESS);
    }

    @Override
    public ResponseEntity<SearchResponse> searchUserByUserName(SearchUserRequestDTO searchUserRequestDTO) {
        SearchResponse sreachResponse = new SearchResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = userRepository.findOneByUserName(userDetails.getUsername());
        if (user == null){
            sreachResponse.setCode(Constants.CODE_ERROR);
            sreachResponse.setMessage(Constants.MESS_010);
            return new ResponseEntity<>(sreachResponse, HttpStatus.OK);
        }
        Pageable pageable = PageRequest.of(searchUserRequestDTO.getPage() - 1 , Constants.LIMIT);
        Page<User> users;
        if ( searchUserRequestDTO.getSearch().isEmpty()){
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByUserNameContaining(searchUserRequestDTO.getSearch(), pageable);
        }
        List<User> userList = users.getContent();
        List<UserInfo>  listUserInfo = userList.stream()
                .map(this::convertUserToUserInfo)
                .collect(Collectors.toList());
        sreachResponse.setListUser(listUserInfo);
        sreachResponse.setTotalPage(users.getTotalPages());
        sreachResponse.setCode(Constants.CODE_OK);
        return new ResponseEntity<>(sreachResponse, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> updatePassword(PasswordChangeDTO passwordChangeDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthDetails userDetails = (UserAuthDetails) authentication.getPrincipal();
        User user = findOneById(Long.parseLong(userDetails.getUserID()));
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(passwordChangeDTO.getOldPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.UPDATE_PASSWORD_ERR);
        }
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(Constants.UPDATE_PASSWORD_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<?> forgotPassword(String email) {
        User user = userRepository.findOneByEmail(email);
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.LOGIN_USER_NOT_FOUND);
        }
        String newPassword = RandomStringUtils.random(8, true, true);
        if ( mailService.sendNewPassword(user.getEmail(), user.getUserName(), newPassword,SendCodeType.RECOVERY)){
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(Constants.FORGOT_PASSWORD_SUCCESS);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Constants.SEND_EMAIL_FAILE);
    }

    public User findOneById(Long id) {
        return userRepository.findOneById(id);
    }

    public User findUserByToken(String token){
        return  userRepository.findOneByToken(token);
    }

    private String validateInfo(User user){
        if(existsByEmail(user.getEmail())){
            return Constants.EMAIL_IS_EXISTS;
        }
        return null;
    }

    private User convertUserDtoToUser(RegisterUserRequest userDTO){
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUserName(userDTO.getUserName());
        if(userDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return user;
    }
    private static boolean isImageFile(String contentType) throws IOException {;
        return contentType != null &&
                (contentType.equals("image/jpeg") || contentType.equals("image/png"));
    }
    private UserInfo convertUserToUserInfo(User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(String.valueOf(user.getId()));
        userInfo.setUserName(user.getUserName());
        userInfo.setBirthday(user.getBirthday() == null ? null : user.getBirthday().toString());
        userInfo.setAddress(user.getAddress());
        userInfo.setJob(user.getJob());
        userInfo.setSex(getGenderById(user.getSex()).name());
        userInfo.setPhone(user.getPhone());
        userInfo.setAvata(user.getImage());

        return userInfo;
    }
}
