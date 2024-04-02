package com.example.websocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ComfirmOTPRequest {
    private String email;
    private String otp;
}
