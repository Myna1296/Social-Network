package com.example.websocialnetwork.dto.request;

import com.example.websocialnetwork.validation.FieldMatch;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch.List({
        @FieldMatch(first = "password", second = "passwordConfirmation", message = "Password fields must match")
})
public class RegisterUserRequestView {
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "Email cannot be blank")
    @Length(min = 10, max = 50, message = "Emails must be between 10 and 50 words in length")
    @Email(message = "Email format is not correct example@examle.com, example@examle.co,...",
            regexp = "^[a-zA-Z0-9_]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @Length(min = 6, max = 100, message = "Password must have a length between 6 and 100")
    private String password;

    @NotEmpty(message = "User name cannot be blank")
    @NotBlank(message = "User name cannot be blank")
    @Length(min = 5, max = 15, message = "User name must be between 5 and 15 words in length")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Username cannot contain forbidden characters")
    private String userName;

    @NotNull
    @Size(min=6, max=100, message = "Min size is 6 and max size is 100")
    private String passwordConfirmation;
}
