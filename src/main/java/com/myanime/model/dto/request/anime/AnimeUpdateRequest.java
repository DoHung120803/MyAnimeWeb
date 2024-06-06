package com.myanime.model.dto.request.anime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AnimeUpdateRequest {
    String name;
    String description;
    String iframe;
    String thumbnailUrl;
}
