package com.myanime.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnimeCreationRequest {
    private String id;
    private String name;
    private String description;
    private double rate;
    private long views;
    private String iframe;
    private String thumbnailUrl;
}