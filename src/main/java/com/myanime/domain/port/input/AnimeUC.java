package com.myanime.domain.port.input;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.application.rest.requests.anime.AnimeCreationRequest;
import com.myanime.application.rest.requests.anime.AnimeUpdateRequest;
import com.myanime.application.rest.responses.AnimeResponse;
import com.myanime.application.rest.responses.PageResponse;
import com.myanime.infrastructure.models.AnimeModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnimeUC {
    AnimeResponse createAnime(AnimeCreationRequest request);
    void createAnime(List<AnimeCreationRequest> request);
    PageResponse<AnimeResponse> getAnimes(int page, int size) throws JsonProcessingException;
    AnimeResponse getAnime(String id);
    AnimeResponse updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
    PageResponse<AnimeModel> search(String name, Pageable pageable);
    List<AnimeResponse> getTopAnimes(String type) throws JsonProcessingException;
}
