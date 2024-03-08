package com.example.datasocialnetwork.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserInfoResponse {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("birthday")
    @DateTimeFormat(pattern="yyyyMMdd")
    private String birthday ;

    @JsonProperty("address")
    private String address;

    @JsonProperty("job")
    private String job;

    @JsonProperty("sex")
    private String sex;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("avata")
    private String avata;

    @JsonProperty("error")
    private String error;
}
