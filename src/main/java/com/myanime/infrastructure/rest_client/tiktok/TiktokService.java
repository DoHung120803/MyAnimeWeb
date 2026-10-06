package com.myanime.infrastructure.rest_client.tiktok;

import com.myanime.infrastructure.rest_client.tiktok.responses.TikTokRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TiktokService {
    private final TiktokClient tiktokClient;

    public TikTokRecommendResponse recommendVideos() {
        return tiktokClient.recommendVideos();
    }
}
