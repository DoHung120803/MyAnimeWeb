package com.myanime.application.rest.responses;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreResponse {
    String id;
    String name;
    String slug;
    String image;
    String description;
    Integer sort;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

