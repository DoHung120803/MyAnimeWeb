package com.myanime.model.dto.request.anime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AnimeCreationRequest {
    @Size(min = 3, message = "ANIME_NAME_INVALID")
    String name;
    String description;
    @NotEmpty(message = "ANIME_IFRAME_INVALID")
    String iframe;
    @NotEmpty(message = "ANIME_THUMBNAIL_INVALID")
    String thumbnailUrl;
}
