package com.myanime.application.rest.requests.genre;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenreUpdateRequest {
    String name;
    String slug;
    String image;
    String description;
    Integer sort;
    Boolean isActive;
}

