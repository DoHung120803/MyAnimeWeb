package com.myanime.domain.service.redis.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;

import java.util.List;

public interface AnimeRedisService {
    PageResponse<AnimeResponse> getAllAnime(int page, int size) throws JsonProcessingException;
    void saveAllAnime(PageResponse<AnimeResponse> pageResponse) throws JsonProcessingException;
    List<AnimeResponse> getTopAnime(String type) throws JsonProcessingException;
    void saveTopAnime(List<AnimeResponse> animeResponses, String type) throws JsonProcessingException;
}
