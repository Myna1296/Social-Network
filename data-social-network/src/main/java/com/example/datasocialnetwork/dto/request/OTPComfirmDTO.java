package com.example.datasocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OTPComfirmDTO {
    private String email;
    private String otp;
}
