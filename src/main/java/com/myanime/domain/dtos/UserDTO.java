package com.myanime.domain.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime dob;
}
