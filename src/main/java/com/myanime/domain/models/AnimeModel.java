package com.myanime.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnimeModel {
    private String id;
    private String name;
    private String description;
    private double rate;
    private long views;
    private String iframe;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
