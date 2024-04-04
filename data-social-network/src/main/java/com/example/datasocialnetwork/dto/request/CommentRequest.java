package com.example.datasocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommentRequest {
    private int pageIndex;
    private int pageSize;
    private Long statusId;
}
