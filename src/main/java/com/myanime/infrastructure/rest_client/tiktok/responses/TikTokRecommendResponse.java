package com.myanime.infrastructure.rest_client.tiktok.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TikTokRecommendResponse {

    private List<Item> itemList;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {

        private String id;

        private String desc;

        private Author author;

        private Stats stats;

        private Video video;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Author {

        private String nickname;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {

        private String commentCount;

        private String diggCount;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Video {

        private String cover;

        private String dynamicCover;

        private String originCover;

        private String duration;
    }
}