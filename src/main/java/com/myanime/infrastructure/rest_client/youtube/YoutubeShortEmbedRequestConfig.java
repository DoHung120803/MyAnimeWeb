package com.myanime.infrastructure.rest_client.youtube;

import com.fasterxml.jackson.core.type.TypeReference;
import com.myanime.common.utils.JsonUtil;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class YoutubeShortEmbedRequestConfig {

    @Value("${feign.client.youtube.short-videos.headers}")
    private String headersJson;

    @Value("${feign.client.youtube.short-videos.body}")
    private String bodyJson;

    private Map<String, String> headers;
    private String body;


    @PostConstruct
    public void init() {
        headers = JsonUtil.jsonToObject(headersJson, new TypeReference<>() {
        });

        body = bodyJson;
    }

}