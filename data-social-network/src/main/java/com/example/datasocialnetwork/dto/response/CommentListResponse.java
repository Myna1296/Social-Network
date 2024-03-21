package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.CommentDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CommentListResponse {

    private List<CommentDTO> listComment;
    private int code;
    private String message;
    private int page;
    private int totalPage;
}
