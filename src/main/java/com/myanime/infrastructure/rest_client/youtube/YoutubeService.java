package com.myanime.infrastructure.rest_client.youtube;

import com.myanime.infrastructure.rest_client.youtube.responses.YoutubeShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final YoutubeClient youtubeClient;
    private final YoutubeShortEmbedRequestConfig youtubeShortEmbedRequestConfig;

    public YoutubeShortResponse shortVideos() {
        return youtubeClient.shortVideos(false, youtubeShortEmbedRequestConfig.getHeaders(), youtubeShortEmbedRequestConfig.getBody());
    }
}
