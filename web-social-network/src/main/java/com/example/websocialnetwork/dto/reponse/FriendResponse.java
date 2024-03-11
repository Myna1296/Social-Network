package com.example.websocialnetwork.dto.reponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class FriendResponse {
    private int code;
    private String message;
    private List<UserInfo> friendData;
    private long total;
}
