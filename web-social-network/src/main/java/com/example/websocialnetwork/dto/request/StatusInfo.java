package com.example.websocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StatusInfo {
    private Long id;
    private String title;
    private String content;
    private String statusImage;
    private int countLike;
    private int countComment;
    private String createDate;
    private Long userId;
    private String userName;
    private String userAvata;
}
