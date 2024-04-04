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
public class NewCommentRequest {
    @NotEmpty(message = "Comment cannot be empty")
    @NotBlank(message = "Comment cannot be blank")
    @Length(min = 1, max = 500, message = "Comment must be between 1 and 500 words in length")
    private String content;
    private Long statusId;
}
