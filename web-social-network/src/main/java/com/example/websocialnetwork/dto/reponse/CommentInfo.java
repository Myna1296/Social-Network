package com.example.websocialnetwork.dto.reponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentInfo {
    private String content;
    private Long id;
    private Long statusId;
    private Long userId;
    private String userName;
    private String userAvata;
    private String createDate;
}
