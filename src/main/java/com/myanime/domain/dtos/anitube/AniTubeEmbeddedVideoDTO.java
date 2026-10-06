package com.myanime.domain.dtos.anitube;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AniTubeEmbeddedVideoDTO {

    private String id;

    private String desc;

    private Author author;

    private Stats stats;

    private Video video;

    public AniTubeEmbeddedVideoDTO id(String id) {
        this.id = id;
        return this;
    }

    public AniTubeEmbeddedVideoDTO desc(String desc) {
        this.desc = desc;
        return this;
    }

    public AniTubeEmbeddedVideoDTO stats(String commentCount, String diggCount) {
        this.stats = new Stats();
        this.stats.setCommentCount(commentCount);
        this.stats.setDiggCount(diggCount);
        return this;
    }

    public AniTubeEmbeddedVideoDTO author(String nickname) {
        this.author = new Author();
        this.author.setNickname(nickname);
        return this;
    }

    public AniTubeEmbeddedVideoDTO video(
            String cover,
            String dynamicCover,
            String originCover,
            String duration
    ) {
        this.video = new Video();
        this.video.setCover(cover);
        this.video.setDynamicCover(dynamicCover);
        this.video.setOriginCover(originCover);
        this.video.setDuration(duration);
        return this;
    }

    @Getter
    @Setter
    public static class Author {

        private String nickname;
    }

    @Getter
    @Setter
    public static class Stats {

        private String commentCount;

        private String diggCount;
    }

    @Getter
    @Setter
    public static class Video {

        private String cover;

        private String dynamicCover;

        private String originCover;

        private String duration;
    }
}