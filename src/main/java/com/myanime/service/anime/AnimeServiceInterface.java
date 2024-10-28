package com.myanime.service.anime;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myanime.entity.jpa.Anime;
import com.myanime.model.dto.request.anime.AnimeCreationRequest;
import com.myanime.model.dto.request.anime.AnimeUpdateRequest;
import com.myanime.model.dto.response.AnimeResponse;
import com.myanime.model.dto.response.PageResponse;

import java.util.List;

public interface AnimeServiceInterface {
    AnimeResponse createAnime(AnimeCreationRequest request);
    void createAnime(List<AnimeCreationRequest> request);
    PageResponse<AnimeResponse> getAnimes(int page, int size) throws JsonProcessingException;
    AnimeResponse getAnime(String id);
    AnimeResponse updateAnime(String id, AnimeUpdateRequest request);
    void deleteAnime(String id);
    List<AnimeResponse> findAnimeByName(String name);
    List<Anime> getTopViewsAnimes();
    List<Anime> getTopRateAnimes();

}
