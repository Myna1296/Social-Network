package com.example.websocialnetwork.dto.reponse;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CommentResponse {
    private Long commentId;
    private Long userId;
    private String userName;
    private String userImage;
    private String text;
    private String createDate;
}
