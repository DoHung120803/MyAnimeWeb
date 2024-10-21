package com.myanime.entity.elasticsearch;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@Setter
@Builder
@Document(indexName = "my_anime", createIndex = false)
public class AnimeES {
    @Id
    private String id;
    private String name;
    private String description;
    private double rate;
    private long views;
    private String iframe;

    @Field(name = "thumbnail_url")
    private String thumbnailUrl;
}
