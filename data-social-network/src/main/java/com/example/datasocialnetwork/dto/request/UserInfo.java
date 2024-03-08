package com.example.datasocialnetwork.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class UserInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("birthday")
    private LocalDate birthday ;

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
