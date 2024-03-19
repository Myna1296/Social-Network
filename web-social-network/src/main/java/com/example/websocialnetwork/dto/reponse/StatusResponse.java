package com.example.websocialnetwork.dto.reponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StatusResponse {
    private Long statusId;
    private Long userId;
    private String userName;
    private String userImage;
    private String statusImage;
    private String text;
    private String title;
    private int likeCount;
    private int commentCount;
    private String createDate;
}
