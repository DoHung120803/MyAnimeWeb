package com.myanime.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserModel {
    private String id;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDate dob;

    private String email;

    private String avtUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String fullName;
}
