package com.example.datasocialnetwork.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserInfoResponse {

    private String userName;

    private LocalDate birthday ;

    private String address;

    private String job;

    private String sex;

    private String phone;

    private String avata;

    private String error;
}
