package com.example.websocialnetwork.dto.reponse;

import lombok.Data;

import java.util.List;

@Data
public class SearchResponse {
    private List<UserInfo> listUser;
    private long totalPage;
}
