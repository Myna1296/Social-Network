package com.example.websocialnetwork.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

    private Long id;
    @NotEmpty(message = "Title cannot be empty")
    @NotBlank(message = "Title cannot be blank")
    @Length(min = 1, max = 50, message = "Title must be between 1 and 50 words in length")
    private String title;

    @NotEmpty(message = "Content cannot be empty")
    @NotBlank(message = "Content cannot be blank")
    @Length(min = 1, max = 5000, message = "Content must be between 1 and 5000 words in length")
    private String content;

    private MultipartFile image;
}
