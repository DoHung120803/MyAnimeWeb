package com.myanime.service.redis.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myanime.config.caches.CacheComponent;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.model.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AnimeRedisImpl implements AnimeRedisService {

    private final CacheComponent<String, String> cacheComponent;
    private final ObjectMapper objectMapper;
    private static final String CACHE_PREFIX = "GET_ALL_ANIME";

    private String getKey(int page, int size) {
        return String.format("%s_%d_%d", CACHE_PREFIX, page, size);
    }

    @Override
    public PageResponse<AnimeResponse> getAllAnime(int page, int size) throws JsonProcessingException {
        String key = getKey(page, size);
        String data = cacheComponent.get(key); // json

        return data != null ?
                objectMapper.readValue(data, new TypeReference<PageResponse<AnimeResponse>>() {
                })
                : null;
    }

    public void saveAllAnime(PageResponse<AnimeResponse> pageResponse) throws JsonProcessingException {
        String key = getKey(pageResponse.getCurrentPage() - 1, pageResponse.getPageSize());
        String json = objectMapper.writeValueAsString(pageResponse);

        cacheComponent.set(key, json, 1L, TimeUnit.HOURS);
    }
}
