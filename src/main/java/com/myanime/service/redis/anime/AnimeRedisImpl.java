package com.myanime.service.redis.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myanime.config.caches.CacheComponent;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.model.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AnimeRedisImpl implements AnimeRedisService {

    private final CacheComponent<String, String> cacheComponent;
    private final ObjectMapper objectMapper;

    private static final String CACHE_PREFIX = "ANIME_CACHE:";
    private static final String GET_ALL_ANIME_KEYWORD = "GET_ALL_ANIME";
    private static final String GET_TOP_VIEWS_ANIME_KEYWORD = "GET_TOP_VIEWS_ANIME";
    private static final String GET_TOP_RATE_ANIME_KEYWORD = "GET_TOP_RATE_ANIME";


    private String getKey(Object... params) {
        return CACHE_PREFIX + String.join(":", Arrays.stream(params)
                .map(String::valueOf)
                .toArray(String[]::new));
    }

    @Override
    public PageResponse<AnimeResponse> getAllAnime(int page, int size) throws JsonProcessingException {
        String key = getKey(GET_ALL_ANIME_KEYWORD, page, size);
        String data = cacheComponent.get(key); // json

        return data != null ?
                objectMapper.readValue(data, new TypeReference<PageResponse<AnimeResponse>>() {
                })
                : null;
    }

    @Override
    public void saveAllAnime(PageResponse<AnimeResponse> pageResponse) throws JsonProcessingException {
        String key = getKey(
                GET_ALL_ANIME_KEYWORD,
                pageResponse.getCurrentPage() - 1,
                pageResponse.getPageSize()
        );
        String json = objectMapper.writeValueAsString(pageResponse);

        cacheComponent.set(key, json, 1L, TimeUnit.HOURS);
    }

    @Override
    public List<AnimeResponse> getTopAnime(String type) throws JsonProcessingException {
        String key = getTopAnimeKey(type);

        String data = cacheComponent.get(key);

        return data != null ?
                objectMapper.readValue(data, new TypeReference<List<AnimeResponse>>() {
                })
                : null;
    }

    @Override
    public void saveTopAnime(List<AnimeResponse> animeResponses, String type) throws JsonProcessingException {
        String key = getTopAnimeKey(type);
        String json = objectMapper.writeValueAsString(animeResponses);

        cacheComponent.set(key, json, 1L, TimeUnit.HOURS);
    }

    private String getTopAnimeKey(String type) {
        if ("rate".equals(type)) {
            return getKey(GET_TOP_RATE_ANIME_KEYWORD);
        } else {
            return getKey(GET_TOP_VIEWS_ANIME_KEYWORD);
        }
    }

}
