package com.example.websocialnetwork.dto.reponse;

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
