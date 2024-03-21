package com.example.websocialnetwork.dto.reponse;

import com.example.websocialnetwork.dto.CommentDTO;
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
