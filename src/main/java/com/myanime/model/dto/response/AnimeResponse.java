package com.myanime.model.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnimeResponse {
    String id;
    String name;
    String description;
    double rate;
    long views;
    String iframe;
    String thumbnailUrl;
}
