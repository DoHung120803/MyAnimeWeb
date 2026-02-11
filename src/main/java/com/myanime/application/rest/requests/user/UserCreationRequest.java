package com.myanime.application.rest.requests.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 5, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "email cannot be empty")
    String email;

    @NotBlank(message = "firstName cannot be empty")
    String firstName;

    @NotBlank(message = "lastName cannot be empty")
    String lastName;

    LocalDate dob;

    String avtUrl;
}
