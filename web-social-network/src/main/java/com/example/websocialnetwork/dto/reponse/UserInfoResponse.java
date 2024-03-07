package com.example.websocialnetwork.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Data
@ToString
@Builder
@Getter
@Setter
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
