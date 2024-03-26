package com.example.websocialnetwork.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentRequest {
    private int page;
    private Long statusId;
}
