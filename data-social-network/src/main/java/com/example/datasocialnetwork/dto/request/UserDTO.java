package com.example.datasocialnetwork.dto.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;

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

    @PastOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String address;
    private String job;
    private String sex;
    private String phone;
    private String image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO user = (UserDTO) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
