package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.UserInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class SearchResponse {
    private int code;
    private String message;
    private List<UserInfo> listUser;
    private long totalPage;
}
