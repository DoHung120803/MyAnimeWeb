package com.myanime.infrastructure.rest_client.youtube;

import com.myanime.infrastructure.configurations.feign.FeignConfig;
import com.myanime.infrastructure.rest_client.youtube.responses.YoutubeShortResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
        name = "youtube-client",
        url = "${feign.client.youtube.api-host}",
        configuration = FeignConfig.class
)
public interface YoutubeClient {
    @PostMapping(value = "${feign.client.youtube.short-videos.path}", produces = MediaType.APPLICATION_JSON_VALUE)
    YoutubeShortResponse shortVideos(
            @RequestParam("prettyPrint") boolean prettyPrint,
            @RequestHeader Map<String, String> headers,
            @RequestBody String body
    );
}
