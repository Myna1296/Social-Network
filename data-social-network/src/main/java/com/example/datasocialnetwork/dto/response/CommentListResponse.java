package com.example.datasocialnetwork.dto.response;

import com.example.datasocialnetwork.dto.request.CommentDTO;
import com.example.datasocialnetwork.dto.request.CommentInfo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class CommentListResponse {

    private List<CommentInfo> listComment;
    private int totalPage;
}
