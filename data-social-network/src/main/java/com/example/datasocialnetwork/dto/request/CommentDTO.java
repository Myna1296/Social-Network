package com.example.datasocialnetwork.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
public class CommentDTO {
    @NotEmpty(message = "Comment cannot be empty")
    @NotBlank(message = "Comment cannot be blank")
    @Length(min = 1, max = 50, message = "0Comment must be between 1 and 500 words in length")
    private String content;

    private Long id;
    private Long statusId;
    private Long userId;
    private String userName;
    private String userAvata;
    private String createDate;
}
