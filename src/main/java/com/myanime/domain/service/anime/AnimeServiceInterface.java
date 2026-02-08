package com.myanime.domain.service.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.domain.models.AnimeModel;

import java.util.List;

public interface AnimeServiceInterface {
    AnimeResponse createAnime(AnimeCreationRequest request);
    void createAnime(List<AnimeCreationRequest> request);
    PageResponse<AnimeResponse> getAnimes(int page, int size) throws JsonProcessingException;
    AnimeResponse getAnime(String id);
    AnimeResponse updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
    PageResponse<AnimeModel> search(String name);
    List<AnimeResponse> getTopAnimes(String type) throws JsonProcessingException;
}
