package com.myanime.infrastructure.rest_client.tiktok;

import com.myanime.infrastructure.configurations.feign.FeignConfig;
import com.myanime.infrastructure.rest_client.tiktok.responses.TikTokRecommendResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "tiktok-client",
        url = "${feign.client.tiktok.api-host}",
        configuration = FeignConfig.class
)
public interface TiktokClient {
    @PostMapping(value = "${feign.client.tiktok.recommend-videos-path}", produces = MediaType.APPLICATION_JSON_VALUE)
    TikTokRecommendResponse recommendVideos();
}
