package com.example.datasocialnetwork.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserInfoResponse {

    @JsonProperty("user_name")
    private String userName;

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
