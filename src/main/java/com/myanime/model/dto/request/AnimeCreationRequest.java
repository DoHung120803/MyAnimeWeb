package com.myanime.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnimeCreationRequest {
    @Size(min = 3, message = "ANIME_NAME_INVALID")
    private String name;
    private String description;
    @NotEmpty(message = "ANIME_IFRAME_INVALID")
    private String iframe;
    @NotEmpty(message = "ANIME_THUMBNAIL_INVALID")
    private String thumbnailUrl;
}
