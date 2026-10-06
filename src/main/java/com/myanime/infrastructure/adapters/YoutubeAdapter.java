package com.myanime.infrastructure.adapters;

import com.myanime.domain.dtos.anitube.AniTubeEmbeddedVideoDTO;
import com.myanime.domain.port.output.YoutubeRepository;
import com.myanime.infrastructure.rest_client.youtube.YoutubeService;
import com.myanime.infrastructure.rest_client.youtube.responses.YoutubeShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YoutubeAdapter implements YoutubeRepository {

    private final YoutubeService youtubeService;

    @Override
    public List<AniTubeEmbeddedVideoDTO> getShortVideos() {
        YoutubeShortResponse ytbShorts = youtubeService.shortVideos();

        if (ytbShorts == null) {
            return List.of();
        }

        return ytbShorts.getEntries().stream()
                .map(entry -> new AniTubeEmbeddedVideoDTO()
                        .id(entry.getVideoId())
                        .desc(entry.getTitle())
                        .author(entry.getAuthorName())
                        .stats(entry.getCommentCount(), entry.getLikeCount())
                        .video(entry.getCoverUrl(), null, null, entry.getLengthSeconds())
                )
                .toList();

    }
}
