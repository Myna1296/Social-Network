package com.example.websocialnetwork.dto;
import com.example.websocialnetwork.validation.FieldMatch;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch.List({
        @FieldMatch(first = "password", second = "passwordConfirmation", message = "The password fields must match")
})
public class PasswordChangeDTO {

    @NotNull
    @Size(min=6, max=100, message = "Min size is 6 and max size is 100")
    private String oldPassword;

    @NotNull
    @Size(min=6, max=100, message = "Min size is 6 and max size is 100")
    private String password;

    @NotNull
    @Size(min=6, max=100, message = "Min size is 6 and max size is 100")
    private String passwordConfirmation;
}
